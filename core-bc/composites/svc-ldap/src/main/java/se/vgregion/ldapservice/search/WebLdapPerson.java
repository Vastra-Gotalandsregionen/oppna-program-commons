package se.vgregion.ldapservice.search;

public class WebLdapPerson extends AbstractPerson {

    @ExplicitLdapName("displayName")
    private String displayName;
    @ExplicitLdapName("hsaPersonIdentityNumber")
    private String hsaPersonIdentityNumber;
    @ExplicitLdapName("mailFile")
    private String mailFile;
    @ExplicitLdapName("givenName")
    private String givenName;
    @ExplicitLdapName("vgrAnstform")
    private String vgrAnstform;
    @ExplicitLdapName("objectClass")
    private String objectClass;
    @ExplicitLdapName("vgrFormansgrupp")
    private String vgrFormansgrupp;
    @ExplicitLdapName("userPassword")
    private String userPassword;
    @ExplicitLdapName("ou")
    private String ou;
    @ExplicitLdapName("vgrStrukturPersonDN")
    private String vgrStrukturPersonDN;
    @ExplicitLdapName("uid")
    private String uid;
    @ExplicitLdapName("mail")
    private String mail;
    @ExplicitLdapName("cn")
    private String cn;
    @ExplicitLdapName("mailServer")
    private String mailServer;
    @ExplicitLdapName("vgrStrukturPerson")
    private String vgrStrukturPerson;
    @ExplicitLdapName("labeledURI")
    private String labeledURI;
    @ExplicitLdapName("vgrTitleCode")
    private String vgrTitleCode;
    @ExplicitLdapName("vgr-id")
    private String vgrId;
    @ExplicitLdapName("vgrAnsvarsnummer")
    private String vgrAnsvarsnummer;
    @ExplicitLdapName("vgrAO3kod")
    private String vgrAO3kod;
    @ExplicitLdapName("o")
    private String o;
    @ExplicitLdapName("l")
    private String l;
    @ExplicitLdapName("hsaIdentity")
    private String hsaIdentity;
    @ExplicitLdapName("sun-fm-saml2-nameid-info")
    private String sunFmSaml2NameidInfo;
    @ExplicitLdapName("sun-fm-saml2-nameid-infokey")
    private String sunFmSaml2NameidInfokey;
    @ExplicitLdapName("fullName")
    private String fullName;
    @ExplicitLdapName("strukturGrupp")
    private String strukturGrupp;
    @ExplicitLdapName("sn")
    private String sn;
    @ExplicitLdapName("title")
    private String title;
    @ExplicitLdapName("vgrAdminType") private String vgrAdminType;
    @ExplicitLdapName("vgrLabeledURI") private String vgrLabeledURI;

    /**
     * Gets the gender.
     *
     * @return the gender
     */
    public Gender getGender() {

        final int hsaPersonIdentityNumberMaxLength = 12;

        if (hsaPersonIdentityNumber == null
                || "".equals(hsaPersonIdentityNumber)
                || hsaPersonIdentityNumber.length() != hsaPersonIdentityNumberMaxLength) {
            return Gender.UNKNOWN;
        }

        final int hsaPersonIdentityNumberLength = 10;
        char c = hsaPersonIdentityNumber.charAt(hsaPersonIdentityNumberLength);
        if (!Character.isDigit(c)) {
            return Gender.UNKNOWN;
        }
        int i = Integer.parseInt(Character.toString(c));
        if (i % 2 == 0) {
            return Gender.FEMALE;
        }
        return Gender.MALE;
    }

    /**
     * Gets the birth year.
     *
     * @return the birth year
     */
    public Short getBirthYear() {

        final int three = 3;
        final int four = 4;


        if (hsaPersonIdentityNumber != null && hsaPersonIdentityNumber.length() > three
                && Character.isDigit(hsaPersonIdentityNumber.charAt(0))
                && Character.isDigit(hsaPersonIdentityNumber.charAt(1))
                && Character.isDigit(hsaPersonIdentityNumber.charAt(2))
                && Character.isDigit(hsaPersonIdentityNumber.charAt(three))) {
            return Short.parseShort(hsaPersonIdentityNumber.substring(0, four));
        }
        return null;
    }

    public String getVgrAdminType() {
        return vgrAdminType;
    }

    public void setVgrAdminType(String vgrAdminType) {
        this.vgrAdminType = vgrAdminType;
    }

    public String getVgrLabeledURI() {
        return vgrLabeledURI;
    }

    public void setVgrLabeledURI(String vgrLabeledURI) {
        this.vgrLabeledURI = vgrLabeledURI;
    }

    public String getL() {
        return l;
    }

    public void setL(String l) {
        this.l = l;
    }

    public static enum Gender {
        MALE, FEMALE, UNKNOWN
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getHsaPersonIdentityNumber() {
        return hsaPersonIdentityNumber;
    }

    public void setHsaPersonIdentityNumber(String hsaPersonIdentityNumber) {
        this.hsaPersonIdentityNumber = hsaPersonIdentityNumber;
    }

    public String getMailFile() {
        return mailFile;
    }

    public void setMailFile(String mailFile) {
        this.mailFile = mailFile;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
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

    public String getVgrFormansgrupp() {
        return vgrFormansgrupp;
    }

    public void setVgrFormansgrupp(String vgrFormansgrupp) {
        this.vgrFormansgrupp = vgrFormansgrupp;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getOu() {
        return ou;
    }

    public void setOu(String ou) {
        this.ou = ou;
    }

    public String getVgrStrukturPersonDN() {
        return vgrStrukturPersonDN;
    }

    public void setVgrStrukturPersonDN(String vgrStrukturPersonDN) {
        this.vgrStrukturPersonDN = vgrStrukturPersonDN;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getMailServer() {
        return mailServer;
    }

    public void setMailServer(String mailServer) {
        this.mailServer = mailServer;
    }

    public String getVgrStrukturPerson() {
        return vgrStrukturPerson;
    }

    public void setVgrStrukturPerson(String vgrStrukturPerson) {
        this.vgrStrukturPerson = vgrStrukturPerson;
    }

    public String getLabeledURI() {
        return labeledURI;
    }

    public void setLabeledURI(String labeledURI) {
        this.labeledURI = labeledURI;
    }

    public String getVgrTitleCode() {
        return vgrTitleCode;
    }

    public void setVgrTitleCode(String vgrTitleCode) {
        this.vgrTitleCode = vgrTitleCode;
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

    public String getO() {
        return o;
    }

    public void setO(String o) {
        this.o = o;
    }

    public String getHsaIdentity() {
        return hsaIdentity;
    }

    public void setHsaIdentity(String hsaIdentity) {
        this.hsaIdentity = hsaIdentity;
    }

    public String getSunFmSaml2NameidInfo() {
        return sunFmSaml2NameidInfo;
    }

    public void setSunFmSaml2NameidInfo(String sunFmSaml2NameidInfo) {
        this.sunFmSaml2NameidInfo = sunFmSaml2NameidInfo;
    }

    public String getSunFmSaml2NameidInfokey() {
        return sunFmSaml2NameidInfokey;
    }

    public void setSunFmSaml2NameidInfokey(String sunFmSaml2NameidInfokey) {
        this.sunFmSaml2NameidInfokey = sunFmSaml2NameidInfokey;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getStrukturGrupp() {
        return strukturGrupp;
    }

    public void setStrukturGrupp(String strukturGrupp) {
        this.strukturGrupp = strukturGrupp;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}