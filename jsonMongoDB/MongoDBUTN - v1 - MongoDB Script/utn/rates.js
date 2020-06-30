use utn;

db.createCollection( "rates",{
    "storageEngine": {
        "wiredTiger": {}
    },
    "capped": false,
    "validator": {
        "$jsonSchema": {
            "bsonType": "object",
            "title": "rates",
            "additionalProperties": false,
            "properties": {
                "_id": {
                    "bsonType": "objectId"
                },
                "id": {
                    "bsonType": "objectId"
                },
                "type": {
                    "bsonType": "string"
                },
                "pricexmin": {
                    "bsonType": "double"
                },
                "cost": {
                    "bsonType": "double"
                },
                "destinationcity": {
                    "bsonType": "objectId"
                },
                "origincity": {
                    "bsonType": "objectId"
                }
            },
            "required": [
                "id"
            ],
            "dependencies": {
                "id": [
                    "type"
                ]
            }
        }
    },
    "validationLevel": "off",
    "validationAction": "warn"
});