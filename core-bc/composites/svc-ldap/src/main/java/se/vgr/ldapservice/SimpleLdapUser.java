package se.vgr.ldapservice;

public class SimpleLdapUser extends LdapUserEntryImpl {

  public SimpleLdapUser(String dn) {
    super(dn);
  }

  String cn;
  String mail;
  String telephoneNumber;

  public String getCn() {
    return cn;
  }

  public void setCn(String cn) {
    this.cn = cn;
  }

  public String getMail() {
    return mail;
  }

  public void setMail(String mail) {
    this.mail = mail;
  }

  public String getTelephoneNumber() {
    return telephoneNumber;
  }

  public void setTelephoneNumber(String telephoneNumber) {
    this.telephoneNumber = telephoneNumber;
  }
}
