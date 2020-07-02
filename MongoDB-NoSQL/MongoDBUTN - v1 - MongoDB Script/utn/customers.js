use utnphone;

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
                },
                "city": {
                    "bsonType": "array",
                    "additionalItems": true,
                    "minItems": 0,
                    "maxItems": 10,
                    "uniqueItems": false,
                    "items": [
                        {
                            "bsonType": "objectId"
                        },
                        {
                            "bsonType": "string"
                        },
                        {
                            "bsonType": "array",
                            "additionalItems": true,
                            "uniqueItems": false,
                            "items": [
                                {
                                    "bsonType": "objectId"
                                },
                                {
                                    "bsonType": "string"
                                }
                            ]
                        }
                    ]
                },
                "line": {
                    "bsonType": "array",
                    "additionalItems": true,
                    "uniqueItems": false,
                    "items": [
                        {
                            "bsonType": "objectId"
                        },
                        {
                            "bsonType": "string"
                        }
                    ]
                }
            }
        }
    },
    "validationLevel": "off",
    "validationAction": "warn"
});