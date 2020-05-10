
/-------------roles--------------------------------------------------
INSERT INTO `roles` (`id`, `rol`, `create_at`, `update_at`)
        VALUES (NULL, 'Cliente', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO `roles` (`id`, `rol`, `create_at`, `update_at`)
        VALUES (NULL, 'Empleado', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

/-------------users----------------------------------------------
INSERT INTO `users` (`iduser`, `user`, `password`, `create_at`, `update_at`)
        VALUES (NULL, 'patriciodiaz', 'patriciodiaz', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO `users` (`iduser`, `user`, `password`, `create_at`, `update_at`)
        VALUES (NULL, 'marianozzz', '123zzz', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

/------------roles_user------------------------------------------------
INSERT INTO `roles_users` (`rolid`, `iduser`, `create_at`, `update_at`)
        VALUES ('1', '1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO `roles_users` (`rolid`, `iduser`, `create_at`, `update_at`)
        VALUES ('2', '2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);