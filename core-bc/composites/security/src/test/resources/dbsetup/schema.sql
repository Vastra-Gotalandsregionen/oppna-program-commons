DROP TABLE vgr_user_site_credential if exists;

CREATE TABLE vgr_user_site_credential
(
  id bigint NOT NULL,
  site_key character varying(50) NOT NULL,
  site_password character varying(256),
  site_user character varying(256),
  uid character varying(50) NOT NULL,
  CONSTRAINT vgr_user_site_credential_pkey PRIMARY KEY (id),
  CONSTRAINT vgr_user_site_credential_uid_key UNIQUE (uid, site_key)
);

DROP TABLE vgr_site_key;

CREATE TABLE vgr_site_key
(
  id bigint NOT NULL,
  active boolean,
  description character varying(1024),
  site_key character varying(50) NOT NULL,
  title character varying(50) NOT NULL,
  screennameonly boolean,
  suggestscreenname boolean,
  CONSTRAINT vgr_site_key_pkey PRIMARY KEY (id),
  CONSTRAINT vgr_site_key_site_key_key UNIQUE (site_key),
  CONSTRAINT vgr_site_key_title_key UNIQUE (title)
);