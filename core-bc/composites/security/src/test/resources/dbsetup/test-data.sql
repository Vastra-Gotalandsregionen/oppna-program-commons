
insert into vgr_user_site_credential (id, uid, site_key, site_user, site_password)
 values (1, 'test-uid', 'test-key', 'test-user', 'test-pw');
insert into vgr_user_site_credential (id, uid, site_key, site_user, site_password)
 values (2, 'test-uid', 'test-key2', 'test-user', 'test-pw');
insert into vgr_user_site_credential (id, uid, site_key, site_user, site_password)
 values (3, 'test-uid2','test-key2', 'test-user', 'test-pw');

insert into vgr_site_key (id, active, description, site_key, title, screennameonly, suggestscreenname)
 values (1, true, 'description', 'test-key', 'Test key 1', true, false);
insert into vgr_site_key (id, active, description, site_key, title, screennameonly, suggestscreenname)
 values (2, true, 'description', 'test-key2', 'Test key 2', false, true);
insert into vgr_site_key (id, active, description, site_key, title, screennameonly, suggestscreenname)
 values (3, true, 'description', 'test-key3', 'Test key 3', true, true);
insert into vgr_site_key (id, active, description, site_key, title, screennameonly, suggestscreenname)
 values (4, true, 'description', 'test-key4', 'Test key 4', false, false);
