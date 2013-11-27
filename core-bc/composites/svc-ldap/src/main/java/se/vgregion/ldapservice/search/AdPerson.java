package se.vgregion.ldapservice.search;

/**
 * Created with IntelliJ IDEA.
 * User: clalu4
 * Date: 2013-11-27
 * Time: 08:31
 * To change this template use File | Settings | File Templates.
 */
public class AdPerson extends AbstractPerson {

    @ExplicitLdapName("userPrincipalName")
    private String userPrincipalName;

    @ExplicitLdapName("userCertificate")
    private byte[] userCertificate;

    @ExplicitLdapName("mailNickname")
    private String mailNickname;

    @ExplicitLdapName("showInAddressBook")
    private String showInAddressBook;

    @ExplicitLdapName("homeDirectory")
    private String homeDirectory;

    @ExplicitLdapName("homeDrive")
    private String homeDrive;

    @ExplicitLdapName("uSNChanged")
    private String uSNChanged;

    @ExplicitLdapName("msExchUserCulture")
    private String msExchUserCulture;

    @ExplicitLdapName("homeMTA")
    private String homeMTA;

    @ExplicitLdapName("msRTCSIP-PrimaryUserAddress")
    private String msRTCSIPPrimaryUserAddress;

    @ExplicitLdapName("msExchPoliciesExcluded")
    private String msExchPoliciesExcluded;

    @ExplicitLdapName("objectGUID")
    private String objectGUID;

    @ExplicitLdapName("msRTCSIP-PrimaryHomeServer")
    private String msRTCSIPPrimaryHomeServer;

    @ExplicitLdapName("uSNCreated")
    private String uSNCreated;

    @ExplicitLdapName("primaryGroupID")
    private String primaryGroupID;

    @ExplicitLdapName("streetAddress")
    private String streetAddress;

    @ExplicitLdapName("title")
    private String title;

    @ExplicitLdapName("msExchUMDtmfMap")
    private String msExchUMDtmfMap;

    @ExplicitLdapName("distinguishedName")
    private String distinguishedName;

    @ExplicitLdapName("msExchELCMailboxFlags")
    private String msExchELCMailboxFlags;

    @ExplicitLdapName("mail")
    private String mail;

    @ExplicitLdapName("msExchVersion")
    private String msExchVersion;

    @ExplicitLdapName("lastLogon")
    private String lastLogon;

    @ExplicitLdapName("badPasswordTime")
    private String badPasswordTime;

    @ExplicitLdapName("msExchHomeServerName")
    private String msExchHomeServerName;

    @ExplicitLdapName("msExchTextMessagingState")
    private String msExchTextMessagingState;

    @ExplicitLdapName("mobile")
    private String mobile;

    @ExplicitLdapName("department")
    private String department;

    @ExplicitLdapName("pwdLastSet")
    private String pwdLastSet;

    @ExplicitLdapName("instanceType")
    private String instanceType;

    @ExplicitLdapName("msRTCSIP-DeploymentLocator")
    private String msRTCSIPDeploymentLocator;

    @ExplicitLdapName("cn")
    private String cn;

    @ExplicitLdapName("mDBUseDefaults")
    private String mDBUseDefaults;

    @ExplicitLdapName("msRTCSIP-OptionFlags")
    private String msRTCSIPOptionFlags;

    @ExplicitLdapName("msExchRecipientTypeDetails")
    private String msExchRecipientTypeDetails;

    @ExplicitLdapName("postalCode")
    private String postalCode;

    @ExplicitLdapName("legacyExchangeDN")
    private String legacyExchangeDN;

    @ExplicitLdapName("initials")
    private String initials;

    @ExplicitLdapName("msExchWhenMailboxCreated")
    private String msExchWhenMailboxCreated;

    @ExplicitLdapName("l")
    private String l;

    @ExplicitLdapName("dSCorePropagationData")
    private String dSCorePropagationData;

    @ExplicitLdapName("employeeType")
    private String employeeType;

    @ExplicitLdapName("msDS-AuthenticatedAtDC")
    private String msDSAuthenticatedAtDC;

    @ExplicitLdapName("company")
    private String company;

    @ExplicitLdapName("countryCode")
    private String countryCode;

    @ExplicitLdapName("lastLogoff")
    private String lastLogoff;

    @ExplicitLdapName("msRTCSIP-UserRoutingGroupId")
    private String msRTCSIPUserRoutingGroupId;

    @ExplicitLdapName("physicalDeliveryOfficeName")
    private String physicalDeliveryOfficeName;

    @ExplicitLdapName("badPwdCount")
    private String badPwdCount;

    @ExplicitLdapName("msExchUserAccountControl")
    private String msExchUserAccountControl;

    @ExplicitLdapName("name")
    private String name;

    @ExplicitLdapName("codePage")
    private String codePage;

    @ExplicitLdapName("division")
    private String division;

    @ExplicitLdapName("msExchRecipientDisplayType")
    private String msExchRecipientDisplayType;

    @ExplicitLdapName("lastLogonTimestamp")
    private String lastLogonTimestamp;

    @ExplicitLdapName("sAMAccountType")
    private String sAMAccountType;

    @ExplicitLdapName("accountExpires")
    private String accountExpires;

    @ExplicitLdapName("msExchMailboxGuid")
    private String msExchMailboxGuid;

    @ExplicitLdapName("whenChanged")
    private String whenChanged;

    @ExplicitLdapName("homeMDB")
    private String homeMDB;

    @ExplicitLdapName("logonCount")
    private String logonCount;

    @ExplicitLdapName("proxyAddresses")
    private String proxyAddresses;

    @ExplicitLdapName("sAMAccountName")
    private String sAMAccountName;

    @ExplicitLdapName("msRTCSIP-InternetAccessEnabled")
    private String msRTCSIPInternetAccessEnabled;

    @ExplicitLdapName("msRTCSIP-FederationEnabled")
    private String msRTCSIPFederationEnabled;

    @ExplicitLdapName("whenCreated")
    private String whenCreated;

    @ExplicitLdapName("objectClass")
    private String objectClass;

    @ExplicitLdapName("sn")
    private String sn;

    @ExplicitLdapName("departmentNumber")
    private String departmentNumber;

    @ExplicitLdapName("protocolSettings")
    private String protocolSettings;

    @ExplicitLdapName("lockoutTime")
    private String lockoutTime;

    @ExplicitLdapName("objectSid")
    private String objectSid;

    @ExplicitLdapName("objectCategory")
    private String objectCategory;

    @ExplicitLdapName("givenName")
    private String givenName;

    @ExplicitLdapName("displayName")
    private String displayName;

    @ExplicitLdapName("memberOf")
    private String memberOf;

    @ExplicitLdapName("msExchMailboxSecurityDescriptor")
    private String msExchMailboxSecurityDescriptor;

    @ExplicitLdapName("msExchRBACPolicyLink")
    private String msExchRBACPolicyLink;

    @ExplicitLdapName("userAccountControl")
    private String userAccountControl;

    @ExplicitLdapName("msRTCSIP-UserPolicies")
    private String msRTCSIPUserPolicies;

    @ExplicitLdapName("msRTCSIP-UserEnabled")
    private String msRTCSIPUserEnabled;


    public String getUserPrincipalName() {
        return userPrincipalName;
    }

    public void setUserPrincipalName(String userPrincipalName) {
        this.userPrincipalName = userPrincipalName;
    }

    public byte[] getUserCertificate() {
        return userCertificate;
    }

    public void setUserCertificate(byte[] userCertificate) {
        this.userCertificate = userCertificate;
    }

    public String getMailNickname() {
        return mailNickname;
    }

    public void setMailNickname(String mailNickname) {
        this.mailNickname = mailNickname;
    }

    public String getShowInAddressBook() {
        return showInAddressBook;
    }

    public void setShowInAddressBook(String showInAddressBook) {
        this.showInAddressBook = showInAddressBook;
    }

    public String getHomeDirectory() {
        return homeDirectory;
    }

    public void setHomeDirectory(String homeDirectory) {
        this.homeDirectory = homeDirectory;
    }

    public String getHomeDrive() {
        return homeDrive;
    }

    public void setHomeDrive(String homeDrive) {
        this.homeDrive = homeDrive;
    }

    public String getuSNChanged() {
        return uSNChanged;
    }

    public void setuSNChanged(String uSNChanged) {
        this.uSNChanged = uSNChanged;
    }

    public String getMsExchUserCulture() {
        return msExchUserCulture;
    }

    public void setMsExchUserCulture(String msExchUserCulture) {
        this.msExchUserCulture = msExchUserCulture;
    }

    public String getHomeMTA() {
        return homeMTA;
    }

    public void setHomeMTA(String homeMTA) {
        this.homeMTA = homeMTA;
    }

    public String getMsRTCSIPPrimaryUserAddress() {
        return msRTCSIPPrimaryUserAddress;
    }

    public void setMsRTCSIPPrimaryUserAddress(String msRTCSIPPrimaryUserAddress) {
        this.msRTCSIPPrimaryUserAddress = msRTCSIPPrimaryUserAddress;
    }

    public String getMsExchPoliciesExcluded() {
        return msExchPoliciesExcluded;
    }

    public void setMsExchPoliciesExcluded(String msExchPoliciesExcluded) {
        this.msExchPoliciesExcluded = msExchPoliciesExcluded;
    }

    public String getObjectGUID() {
        return objectGUID;
    }

    public void setObjectGUID(String objectGUID) {
        this.objectGUID = objectGUID;
    }

    public String getMsRTCSIPPrimaryHomeServer() {
        return msRTCSIPPrimaryHomeServer;
    }

    public void setMsRTCSIPPrimaryHomeServer(String msRTCSIPPrimaryHomeServer) {
        this.msRTCSIPPrimaryHomeServer = msRTCSIPPrimaryHomeServer;
    }

    public String getuSNCreated() {
        return uSNCreated;
    }

    public void setuSNCreated(String uSNCreated) {
        this.uSNCreated = uSNCreated;
    }

    public String getPrimaryGroupID() {
        return primaryGroupID;
    }

    public void setPrimaryGroupID(String primaryGroupID) {
        this.primaryGroupID = primaryGroupID;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsExchUMDtmfMap() {
        return msExchUMDtmfMap;
    }

    public void setMsExchUMDtmfMap(String msExchUMDtmfMap) {
        this.msExchUMDtmfMap = msExchUMDtmfMap;
    }

    public String getDistinguishedName() {
        return distinguishedName;
    }

    public void setDistinguishedName(String distinguishedName) {
        this.distinguishedName = distinguishedName;
    }

    public String getMsExchELCMailboxFlags() {
        return msExchELCMailboxFlags;
    }

    public void setMsExchELCMailboxFlags(String msExchELCMailboxFlags) {
        this.msExchELCMailboxFlags = msExchELCMailboxFlags;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMsExchVersion() {
        return msExchVersion;
    }

    public void setMsExchVersion(String msExchVersion) {
        this.msExchVersion = msExchVersion;
    }

    public String getLastLogon() {
        return lastLogon;
    }

    public void setLastLogon(String lastLogon) {
        this.lastLogon = lastLogon;
    }

    public String getBadPasswordTime() {
        return badPasswordTime;
    }

    public void setBadPasswordTime(String badPasswordTime) {
        this.badPasswordTime = badPasswordTime;
    }

    public String getMsExchHomeServerName() {
        return msExchHomeServerName;
    }

    public void setMsExchHomeServerName(String msExchHomeServerName) {
        this.msExchHomeServerName = msExchHomeServerName;
    }

    public String getMsExchTextMessagingState() {
        return msExchTextMessagingState;
    }

    public void setMsExchTextMessagingState(String msExchTextMessagingState) {
        this.msExchTextMessagingState = msExchTextMessagingState;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPwdLastSet() {
        return pwdLastSet;
    }

    public void setPwdLastSet(String pwdLastSet) {
        this.pwdLastSet = pwdLastSet;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }

    public String getMsRTCSIPDeploymentLocator() {
        return msRTCSIPDeploymentLocator;
    }

    public void setMsRTCSIPDeploymentLocator(String msRTCSIPDeploymentLocator) {
        this.msRTCSIPDeploymentLocator = msRTCSIPDeploymentLocator;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getmDBUseDefaults() {
        return mDBUseDefaults;
    }

    public void setmDBUseDefaults(String mDBUseDefaults) {
        this.mDBUseDefaults = mDBUseDefaults;
    }

    public String getMsRTCSIPOptionFlags() {
        return msRTCSIPOptionFlags;
    }

    public void setMsRTCSIPOptionFlags(String msRTCSIPOptionFlags) {
        this.msRTCSIPOptionFlags = msRTCSIPOptionFlags;
    }

    public String getMsExchRecipientTypeDetails() {
        return msExchRecipientTypeDetails;
    }

    public void setMsExchRecipientTypeDetails(String msExchRecipientTypeDetails) {
        this.msExchRecipientTypeDetails = msExchRecipientTypeDetails;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getLegacyExchangeDN() {
        return legacyExchangeDN;
    }

    public void setLegacyExchangeDN(String legacyExchangeDN) {
        this.legacyExchangeDN = legacyExchangeDN;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getMsExchWhenMailboxCreated() {
        return msExchWhenMailboxCreated;
    }

    public void setMsExchWhenMailboxCreated(String msExchWhenMailboxCreated) {
        this.msExchWhenMailboxCreated = msExchWhenMailboxCreated;
    }

    public String getL() {
        return l;
    }

    public void setL(String l) {
        this.l = l;
    }

    public String getdSCorePropagationData() {
        return dSCorePropagationData;
    }

    public void setdSCorePropagationData(String dSCorePropagationData) {
        this.dSCorePropagationData = dSCorePropagationData;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }

    public String getMsDSAuthenticatedAtDC() {
        return msDSAuthenticatedAtDC;
    }

    public void setMsDSAuthenticatedAtDC(String msDSAuthenticatedAtDC) {
        this.msDSAuthenticatedAtDC = msDSAuthenticatedAtDC;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getLastLogoff() {
        return lastLogoff;
    }

    public void setLastLogoff(String lastLogoff) {
        this.lastLogoff = lastLogoff;
    }

    public String getMsRTCSIPUserRoutingGroupId() {
        return msRTCSIPUserRoutingGroupId;
    }

    public void setMsRTCSIPUserRoutingGroupId(String msRTCSIPUserRoutingGroupId) {
        this.msRTCSIPUserRoutingGroupId = msRTCSIPUserRoutingGroupId;
    }

    public String getPhysicalDeliveryOfficeName() {
        return physicalDeliveryOfficeName;
    }

    public void setPhysicalDeliveryOfficeName(String physicalDeliveryOfficeName) {
        this.physicalDeliveryOfficeName = physicalDeliveryOfficeName;
    }

    public String getBadPwdCount() {
        return badPwdCount;
    }

    public void setBadPwdCount(String badPwdCount) {
        this.badPwdCount = badPwdCount;
    }

    public String getMsExchUserAccountControl() {
        return msExchUserAccountControl;
    }

    public void setMsExchUserAccountControl(String msExchUserAccountControl) {
        this.msExchUserAccountControl = msExchUserAccountControl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCodePage() {
        return codePage;
    }

    public void setCodePage(String codePage) {
        this.codePage = codePage;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getMsExchRecipientDisplayType() {
        return msExchRecipientDisplayType;
    }

    public void setMsExchRecipientDisplayType(String msExchRecipientDisplayType) {
        this.msExchRecipientDisplayType = msExchRecipientDisplayType;
    }

    public String getLastLogonTimestamp() {
        return lastLogonTimestamp;
    }

    public void setLastLogonTimestamp(String lastLogonTimestamp) {
        this.lastLogonTimestamp = lastLogonTimestamp;
    }

    public String getsAMAccountType() {
        return sAMAccountType;
    }

    public void setsAMAccountType(String sAMAccountType) {
        this.sAMAccountType = sAMAccountType;
    }

    public String getAccountExpires() {
        return accountExpires;
    }

    public void setAccountExpires(String accountExpires) {
        this.accountExpires = accountExpires;
    }

    public String getMsExchMailboxGuid() {
        return msExchMailboxGuid;
    }

    public void setMsExchMailboxGuid(String msExchMailboxGuid) {
        this.msExchMailboxGuid = msExchMailboxGuid;
    }

    public String getWhenChanged() {
        return whenChanged;
    }

    public void setWhenChanged(String whenChanged) {
        this.whenChanged = whenChanged;
    }

    public String getHomeMDB() {
        return homeMDB;
    }

    public void setHomeMDB(String homeMDB) {
        this.homeMDB = homeMDB;
    }

    public String getLogonCount() {
        return logonCount;
    }

    public void setLogonCount(String logonCount) {
        this.logonCount = logonCount;
    }

    public String getProxyAddresses() {
        return proxyAddresses;
    }

    public void setProxyAddresses(String proxyAddresses) {
        this.proxyAddresses = proxyAddresses;
    }

    public String getsAMAccountName() {
        return sAMAccountName;
    }

    public void setsAMAccountName(String sAMAccountName) {
        this.sAMAccountName = sAMAccountName;
    }

    public String getMsRTCSIPInternetAccessEnabled() {
        return msRTCSIPInternetAccessEnabled;
    }

    public void setMsRTCSIPInternetAccessEnabled(String msRTCSIPInternetAccessEnabled) {
        this.msRTCSIPInternetAccessEnabled = msRTCSIPInternetAccessEnabled;
    }

    public String getMsRTCSIPFederationEnabled() {
        return msRTCSIPFederationEnabled;
    }

    public void setMsRTCSIPFederationEnabled(String msRTCSIPFederationEnabled) {
        this.msRTCSIPFederationEnabled = msRTCSIPFederationEnabled;
    }

    public String getWhenCreated() {
        return whenCreated;
    }

    public void setWhenCreated(String whenCreated) {
        this.whenCreated = whenCreated;
    }

    public String getObjectClass() {
        return objectClass;
    }

    public void setObjectClass(String objectClass) {
        this.objectClass = objectClass;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getDepartmentNumber() {
        return departmentNumber;
    }

    public void setDepartmentNumber(String departmentNumber) {
        this.departmentNumber = departmentNumber;
    }

    public String getProtocolSettings() {
        return protocolSettings;
    }

    public void setProtocolSettings(String protocolSettings) {
        this.protocolSettings = protocolSettings;
    }

    public String getLockoutTime() {
        return lockoutTime;
    }

    public void setLockoutTime(String lockoutTime) {
        this.lockoutTime = lockoutTime;
    }

    public String getObjectSid() {
        return objectSid;
    }

    public void setObjectSid(String objectSid) {
        this.objectSid = objectSid;
    }

    public String getObjectCategory() {
        return objectCategory;
    }

    public void setObjectCategory(String objectCategory) {
        this.objectCategory = objectCategory;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getMemberOf() {
        return memberOf;
    }

    public void setMemberOf(String memberOf) {
        this.memberOf = memberOf;
    }

    public String getMsExchMailboxSecurityDescriptor() {
        return msExchMailboxSecurityDescriptor;
    }

    public void setMsExchMailboxSecurityDescriptor(String msExchMailboxSecurityDescriptor) {
        this.msExchMailboxSecurityDescriptor = msExchMailboxSecurityDescriptor;
    }

    public String getMsExchRBACPolicyLink() {
        return msExchRBACPolicyLink;
    }

    public void setMsExchRBACPolicyLink(String msExchRBACPolicyLink) {
        this.msExchRBACPolicyLink = msExchRBACPolicyLink;
    }

    public String getUserAccountControl() {
        return userAccountControl;
    }

    public void setUserAccountControl(String userAccountControl) {
        this.userAccountControl = userAccountControl;
    }

    public String getMsRTCSIPUserPolicies() {
        return msRTCSIPUserPolicies;
    }

    public void setMsRTCSIPUserPolicies(String msRTCSIPUserPolicies) {
        this.msRTCSIPUserPolicies = msRTCSIPUserPolicies;
    }

    public String getMsRTCSIPUserEnabled() {
        return msRTCSIPUserEnabled;
    }

    public void setMsRTCSIPUserEnabled(String msRTCSIPUserEnabled) {
        this.msRTCSIPUserEnabled = msRTCSIPUserEnabled;
    }
}
