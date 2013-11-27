package se.vgregion.ldapservice.search;

/**
 * Created with IntelliJ IDEA.
 * User: clalu4
 * Date: 2013-11-27
 * Time: 08:57
 * To change this template use File | Settings | File Templates.
 */
public class KivPerson extends AbstractPerson {

    @ExplicitLdapName("vgrTilltalskod")
    private String vgrTilltalskod;

    @ExplicitLdapName("hsaPersonIdentityNumber")
    private String hsaPersonIdentityNumber;

    @ExplicitLdapName("givenName")
    private String givenName;

    @ExplicitLdapName("vgrObjectStatusTime")
    private String vgrObjectStatusTime;

    @ExplicitLdapName("vgrAnstform")
    private String vgrAnstform;

    @ExplicitLdapName("objectClass")
    private String objectClass;

    @ExplicitLdapName("userCertificate;binary")
    private byte[] userCertificateBinary;

    @ExplicitLdapName("vgrFormansgrupp")
    private String vgrFormansgrupp;

    @ExplicitLdapName("hsaMifareSerialNumber")
    private String hsaMifareSerialNumber;

    @ExplicitLdapName("vgrOrgRel")
    private String vgrOrgRel;

    @ExplicitLdapName("mail")
    private String mail;

    @ExplicitLdapName("vgrObjectSource")
    private String vgrObjectSource;

    @ExplicitLdapName("vgrStrukturPersonDN")
    private String vgrStrukturPersonDN;

    @ExplicitLdapName("cn")
    private String cn;

    @ExplicitLdapName("initials")
    private String initials;

    @ExplicitLdapName("vgrObjectStatus")
    private String vgrObjectStatus;

    @ExplicitLdapName("vgrStrukturPerson")
    private String vgrStrukturPerson;

    @ExplicitLdapName("userPrincipalName")
    private String userPrincipalName;

    @ExplicitLdapName("vgr-id")
    private String vgrId;

    @ExplicitLdapName("vgrAnsvarsnummer")
    private String vgrAnsvarsnummer;

    @ExplicitLdapName("vgrAO3kod")
    private String vgrAO3kod;

    @ExplicitLdapName("hsaIdentity")
    private String hsaIdentity;

    @ExplicitLdapName("fullName")
    private String fullName;

    @ExplicitLdapName("sn")
    private String sn;

    @ExplicitLdapName("hsaStartDate")
    private String hsaStartDate;


    public String getVgrTilltalskod() {
        return vgrTilltalskod;
    }

    public void setVgrTilltalskod(String vgrTilltalskod) {
        this.vgrTilltalskod = vgrTilltalskod;
    }

    public String getHsaPersonIdentityNumber() {
        return hsaPersonIdentityNumber;
    }

    public void setHsaPersonIdentityNumber(String hsaPersonIdentityNumber) {
        this.hsaPersonIdentityNumber = hsaPersonIdentityNumber;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getVgrObjectStatusTime() {
        return vgrObjectStatusTime;
    }

    public void setVgrObjectStatusTime(String vgrObjectStatusTime) {
        this.vgrObjectStatusTime = vgrObjectStatusTime;
    }

    public String getVgrAnstform() {
        return vgrAnstform;
    }

    public void setVgrAnstform(String vgrAnstform) {
        this.vgrAnstform = vgrAnstform;
    }

    public String getObjectClass() {
        return objectClass;
    }

    public void setObjectClass(String objectClass) {
        this.objectClass = objectClass;
    }

    public byte[] getUserCertificateBinary() {
        return userCertificateBinary;
    }

    public void setUserCertificateBinary(byte[] userCertificateBinary) {
        this.userCertificateBinary = userCertificateBinary;
    }

    public String getVgrFormansgrupp() {
        return vgrFormansgrupp;
    }

    public void setVgrFormansgrupp(String vgrFormansgrupp) {
        this.vgrFormansgrupp = vgrFormansgrupp;
    }

    public String getHsaMifareSerialNumber() {
        return hsaMifareSerialNumber;
    }

    public void setHsaMifareSerialNumber(String hsaMifareSerialNumber) {
        this.hsaMifareSerialNumber = hsaMifareSerialNumber;
    }

    public String getVgrOrgRel() {
        return vgrOrgRel;
    }

    public void setVgrOrgRel(String vgrOrgRel) {
        this.vgrOrgRel = vgrOrgRel;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getVgrObjectSource() {
        return vgrObjectSource;
    }

    public void setVgrObjectSource(String vgrObjectSource) {
        this.vgrObjectSource = vgrObjectSource;
    }

    public String getVgrStrukturPersonDN() {
        return vgrStrukturPersonDN;
    }

    public void setVgrStrukturPersonDN(String vgrStrukturPersonDN) {
        this.vgrStrukturPersonDN = vgrStrukturPersonDN;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getVgrObjectStatus() {
        return vgrObjectStatus;
    }

    public void setVgrObjectStatus(String vgrObjectStatus) {
        this.vgrObjectStatus = vgrObjectStatus;
    }

    public String getVgrStrukturPerson() {
        return vgrStrukturPerson;
    }

    public void setVgrStrukturPerson(String vgrStrukturPerson) {
        this.vgrStrukturPerson = vgrStrukturPerson;
    }

    public String getUserPrincipalName() {
        return userPrincipalName;
    }

    public void setUserPrincipalName(String userPrincipalName) {
        this.userPrincipalName = userPrincipalName;
    }

    public String getVgrId() {
        return vgrId;
    }

    public void setVgrId(String vgrId) {
        this.vgrId = vgrId;
    }

    public String getVgrAnsvarsnummer() {
        return vgrAnsvarsnummer;
    }

    public void setVgrAnsvarsnummer(String vgrAnsvarsnummer) {
        this.vgrAnsvarsnummer = vgrAnsvarsnummer;
    }

    public String getVgrAO3kod() {
        return vgrAO3kod;
    }

    public void setVgrAO3kod(String vgrAO3kod) {
        this.vgrAO3kod = vgrAO3kod;
    }

    public String getHsaIdentity() {
        return hsaIdentity;
    }

    public void setHsaIdentity(String hsaIdentity) {
        this.hsaIdentity = hsaIdentity;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getHsaStartDate() {
        return hsaStartDate;
    }

    public void setHsaStartDate(String hsaStartDate) {
        this.hsaStartDate = hsaStartDate;
    }
}


