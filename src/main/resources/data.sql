insert into cat_deptos_atencion ( departamento_nombre, codigo) values('SOPORTE GENERAL','SGR');
insert into cat_deptos_atencion ( departamento_nombre, codigo) values('SERVIDORES','SRV');
insert into cat_deptos_atencion ( departamento_nombre, codigo) values('TELEFONIA','TEL');
insert into cat_deptos_atencion ( departamento_nombre, codigo) values('COTIZACIONES','CTZ');
insert into cat_deptos_atencion ( departamento_nombre, codigo) values('FALLAS EQUIPOS PERSONALES','FAL');

insert into cat_contacto(contacto) values('TELEFONICO');
insert into cat_contacto(contacto) values('CORREO');
insert into cat_contacto(contacto) values('MENSAJE SMS/WHATSAPP');

insert into cat_status(status,activo, indice) values('EN ESPERA',true,0 );
insert into cat_status(status,activo, indice) values('FINALIZADO',true, 10);


insert into roles(role) values('ADMIN');
insert into roles(role) values('OPERADOR');
insert into roles(role) values('CLIENTE');




insert into operadores (usuario, nombre, apellidos,correo, password, activo) values ('ADMIN','Administrator', 'Administrator', 'admin@user.com', '$2a$10$lhuarN9HVvIpqPBvs3IuKe2t.gW7JnjYRs.SCrVba6UQiH.btgzoW', true);
