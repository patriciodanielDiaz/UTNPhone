use utn;

db.createCollection( "invoices",{
    "storageEngine": {
        "wiredTiger": {}
    },
    "capped": false,
    "validator": {
        "$jsonSchema": {
            "bsonType": "object",
            "title": "invoices",
            "additionalProperties": false,
            "properties": {
                "_id": {
                    "bsonType": "objectId"
                },
                "id": {
                    "bsonType": "objectId"
                },
                "iduser": {
                    "bsonType": "objectId"
                },
                "idline": {
                    "bsonType": "objectId"
                },
                "total": {
                    "bsonType": "double"
                },
                "expirationdate": {
                    "bsonType": "date"
                },
                "date_issued": {
                    "bsonType": "date"
                }
            },
            "required": [
                "id"
            ],
            "dependencies": {
                "id": [
                    "iduser"
                ]
            }
        }
    },
    "validationLevel": "off",
    "validationAction": "warn"
});