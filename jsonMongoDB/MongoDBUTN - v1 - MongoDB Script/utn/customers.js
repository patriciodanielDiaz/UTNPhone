use utn;

db.createCollection( "customers",{
    "storageEngine": {
        "wiredTiger": {}
    },
    "capped": false,
    "validator": {
        "$jsonSchema": {
            "bsonType": "object",
            "title": "customers",
            "additionalProperties": false,
            "properties": {
                "_id": {
                    "bsonType": "objectId"
                },
                "id": {
                    "bsonType": "objectId"
                },
                "name": {
                    "bsonType": "string"
                },
                "lastname": {
                    "bsonType": "string"
                },
                "dni": {
                    "bsonType": "string"
                },
                "password": {
                    "bsonType": "string"
                },
                "usertype": {
                    "bsonType": "string",
                    "enum": [
                        "Empleado",
                        "Cliente"
                    ]
                }
            },
            "required": [
                "id",
                "password"
            ]
        }
    },
    "validationLevel": "off",
    "validationAction": "warn"
});