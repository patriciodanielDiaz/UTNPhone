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
                    "bsonType": "array",
                    "additionalItems": true,
                    "uniqueItems": false,
                    "items": [
                        {
                            "bsonType": "string"
                        },
                        {
                            "bsonType": "string"
                        }
                    ]
                },
                "origincity": {
                    "bsonType": "array",
                    "additionalItems": true,
                    "uniqueItems": false,
                    "items": [
                        {
                            "bsonType": "string"
                        },
                        {
                            "bsonType": "string"
                        }
                    ]
                }
            },
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