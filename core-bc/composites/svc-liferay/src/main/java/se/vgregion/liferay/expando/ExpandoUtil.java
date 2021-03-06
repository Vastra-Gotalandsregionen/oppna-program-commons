package se.vgregion.liferay.expando;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.service.ResourcePermissionLocalService;
import com.liferay.portal.service.ResourcePermissionLocalServiceUtil;
import com.liferay.portal.service.RoleLocalService;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portlet.expando.model.ExpandoColumn;
import com.liferay.portlet.expando.model.ExpandoColumnConstants;
import com.liferay.portlet.expando.model.ExpandoTable;
import com.liferay.portlet.expando.model.ExpandoTableConstants;
import com.liferay.portlet.expando.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Created: 2011-11-19 17:29
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class ExpandoUtil {
    enum Mode {AUTO_CREATE, SET_ONLY}

    private static final Logger LOGGER = LoggerFactory.getLogger(ExpandoUtil.class);

    @Autowired
    protected ExpandoColumnLocalService expandoColumnService;

    @Autowired
    protected ExpandoTableLocalService expandoTableService;

    @Autowired
    protected ExpandoValueLocalService expandoValueService;

    @Autowired
    protected RoleLocalService roleLocalService;

    @Autowired
    protected ResourcePermissionLocalService resourcePermissionLocalService;

    /**
     * Creates an ExpandoUtil instance which is dependent on the Liferay runtime. No additional configuration is needed.
     * @return a new ExpandoUtil instance
     */
    public static ExpandoUtil createDefaultExpandoUtil() {
        ExpandoUtil expandoUtil = new ExpandoUtil();
        expandoUtil.expandoColumnService = ExpandoColumnLocalServiceUtil.getService();
        expandoUtil.expandoTableService = ExpandoTableLocalServiceUtil.getService();
        expandoUtil.expandoValueService = ExpandoValueLocalServiceUtil.getService();
        expandoUtil.roleLocalService = RoleLocalServiceUtil.getService();
        expandoUtil.resourcePermissionLocalService = ResourcePermissionLocalServiceUtil.getService();

        return expandoUtil;
    }

    public void setExpando(String targetClassName, String columnName, Object value, long companyId,
            long classPK, Mode mode) {
        try {
            expandoValueService.addValue(companyId, targetClassName, ExpandoTableConstants.DEFAULT_TABLE_NAME,
                    columnName, classPK, value);
        } catch (Exception e) {
            if (mode == Mode.AUTO_CREATE) {
                // Create expando and try again.
                int expandoType = resolveExpandoType(value.getClass());
                createIfNeeded(companyId, targetClassName, columnName, expandoType);
                setExpando(targetClassName, columnName, value, companyId, classPK, Mode.SET_ONLY);
            } else {
                String msg = String.format("Failed to set expando value [%s, %s, %s, %s, %s]", companyId,
                        targetClassName, columnName, classPK, value);
                log(msg, e);
            }
        }
    }

    public Object getExpando(long companyId, String targetClassName, String columnName, long classPK) {
        try {
            return expandoValueService.getData(companyId, targetClassName,
                    ExpandoTableConstants.DEFAULT_TABLE_NAME, columnName, classPK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<ExpandoColumn> getAllExpando(String targetClassName, long companyId) {
        try {
            return expandoColumnService.getDefaultTableColumns(companyId, targetClassName);
        } catch (Exception e) {
            LOGGER.error("getAllExpando", e);
            throw new RuntimeException(e);
        }
    }

    public void deleteExpando(String targetClassName, String columnName, long companyId) {
        try {
            expandoColumnService.deleteColumn(companyId, targetClassName, ExpandoTableConstants.DEFAULT_TABLE_NAME, columnName);
        } catch (Exception e) {
            LOGGER.error("deleteExpando", e);
            throw new RuntimeException(e);
        }
    }

    private ExpandoColumn createExpandoColumn(long companyId, long tableId, String columnName, int expandoType) {
        try {
            ExpandoColumn expandoColumn = expandoColumnService.addColumn(tableId, columnName, expandoType);
            resourcePermission(companyId, expandoColumn, RoleConstants.USER);

            return expandoColumn;
        } catch (Exception e) {
            LOGGER.error("createExpandoColumn", e);
            throw new RuntimeException(e);
        }
    }

    private void resourcePermission(long companyId, ExpandoColumn expandoColumn, String roleName) {
        try {
            if (roleLocalService != null && resourcePermissionLocalService != null) {
                Role permissionRole = roleLocalService.getRole(companyId, roleName);
                resourcePermissionLocalService.setResourcePermissions(companyId, ExpandoColumn.class.getName(),
                        ResourceConstants.SCOPE_INDIVIDUAL, String.valueOf(expandoColumn.getColumnId()),
                        permissionRole.getRoleId(), new String[]{ActionKeys.VIEW, ActionKeys.UPDATE});
            }
        } catch (Exception e) {
            LOGGER.error("resource permission", e);
            throw new RuntimeException(e);
        }
    }

    private ExpandoTable createExpandoTable(long companyId, String targetClassName) {
        try {
            return expandoTableService.addDefaultTable(companyId, targetClassName);
        } catch (Exception e) {
            LOGGER.error("createExpandoTable", e);
            throw new RuntimeException(e);
        }
    }

    private void createIfNeeded(long companyId, String targetClassName, String columnName, int expandoType) {
        ExpandoTable expandoTable = null;
        try {
            expandoTable = expandoTableService.getDefaultTable(companyId, targetClassName);
        } catch (PortalException e) {
            if (e instanceof com.liferay.portlet.expando.NoSuchTableException) {
                // If table don't exists we try to create it.
                expandoTable = createExpandoTable(companyId, targetClassName);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create ExpandoTable [" + targetClassName + "]", e);
        }

        try {
            expandoColumnService.getColumn(expandoTable.getTableId(), columnName);
      /*Removed for Liferay 6.2

        } catch (PortalException e) {
            if (e instanceof com.liferay.portlet.expando.NoSuchColumnException) {
                // If column don't exists we try to create it.
                createExpandoColumn(companyId, expandoTable.getTableId(), columnName, expandoType);
            }*/
        } catch (Exception e) {
            // Moved to here for LR 6.2
            if (e instanceof com.liferay.portlet.expando.NoSuchColumnException) {
                // If column don't exists we try to create it.
                createExpandoColumn(companyId, expandoTable.getTableId(), columnName, expandoType);
            }else {
                throw new RuntimeException("Failed to create ExpandoColumn [" + columnName + "]", e);
            }
        }
    }

    private int resolveExpandoType(Class valueClass) {

        if (valueClass == String.class) return ExpandoColumnConstants.STRING;
        if (valueClass == Boolean.class) return ExpandoColumnConstants.BOOLEAN;
        if (valueClass == Long.class) return ExpandoColumnConstants.LONG;
        if (valueClass == Integer.class) return ExpandoColumnConstants.INTEGER;
        if (valueClass == Date.class) return ExpandoColumnConstants.DATE;
        if (valueClass == Double.class) return ExpandoColumnConstants.DOUBLE;
        if (valueClass == Short.class) return ExpandoColumnConstants.SHORT;

        if (valueClass == String[].class) return ExpandoColumnConstants.STRING_ARRAY;
        if (valueClass == boolean[].class) return ExpandoColumnConstants.BOOLEAN_ARRAY;
        if (valueClass == long[].class) return ExpandoColumnConstants.LONG_ARRAY;
        if (valueClass == int[].class) return ExpandoColumnConstants.INTEGER_ARRAY;
        if (valueClass == Date[].class) return ExpandoColumnConstants.DATE_ARRAY;
        if (valueClass == double[].class) return ExpandoColumnConstants.DOUBLE_ARRAY;
        if (valueClass == short[].class) return ExpandoColumnConstants.SHORT_ARRAY;

        if (valueClass.isAssignableFrom(Serializable.class)) return ExpandoColumnConstants.STRING;

        String msg = String.format("Class [%s] cannot be used as expando-value.", valueClass.getName());
        throw new IllegalArgumentException(msg);
    }


    private void log(String msg, Exception e) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.warn(msg, e);
        } else {
            LOGGER.warn(msg);
        }
    }
}
