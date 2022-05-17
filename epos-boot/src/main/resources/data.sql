INSERT INTO "public"."people_roles"(
    "id",
    "createddate",
    "isdeleted",
    "updateddate",
    "rolename",
    "createdbyid",
    "updatedbyid"
) VALUES (
    '072548b0-9d29-11ea-bb37-0242ac130002',
    '2012-12-12 00:00:00',
    'false',
    NULL,
    'ADMIN',
    NULL,
    NULL
 );
 INSERT INTO "public"."people"(
    "id",
    "createddate",
    "isdeleted",
    "updateddate",
    "image",
    "name",
    "password",
    "createdbyid",
    "updatedbyid",
    "roleid"
) VALUES (
    'fdb60b84-9d28-11ea-bb37-0242ac130002',
    '1996-10-12 00:00:00',
    'false',
    NULL,
    NULL,
    'celal',
    '$2a$10$MOd21d1T9HvszsXmgyvW0e0rSIf7TfsTgiSKhpnrdVee/ilagV50G',
    NULL,
    NULL,
    '072548b0-9d29-11ea-bb37-0242ac130002'
 );
 INSERT INTO "public"."payment_types"(
    "id",
    "createddate",
    "isdeleted",
    "updateddate",
    "typename",
    "createdbyid",
    "updatedbyid"
) VALUES (
    '1bfd7f88-9e9a-11ea-bb37-0242ac130002',
    '2020-05-25 00:00:00',
    'false',
    NULL,
    'Cash',
    NULL,
    NULL
 );

INSERT INTO "public"."payment_types"(
    "id",
    "createddate",
    "isdeleted",
    "updateddate",
    "typename",
    "createdbyid",
    "updatedbyid"
) VALUES (
    '22212568-9e9a-11ea-bb37-0242ac130002',
    '2020-05-25 00:00:00',
    'false',
    NULL,
    'Credit Cart',
    NULL,
    NULL
 );
/*
 Username : celal
 Password : 1
 */