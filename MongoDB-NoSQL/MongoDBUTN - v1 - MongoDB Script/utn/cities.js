use utn;

db.createCollection( "cities",{
    "storageEngine": {
        "wiredTiger": {}
    },
    "capped": false,
    "validator": {
        "$jsonSchema": {
            "bsonType": "object",
            "title": "cities",
            "additionalProperties": false,
            "properties": {
                "_id": {
                    "bsonType": "objectId"
                },
                "id": {
                    "bsonType": "objectId"
                },
                "city": {
                    "bsonType": "string"
                },
                "province": {
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
                },
                "prefix": {
                    "bsonType": "string"
                }
            }
        }
    },
    "validationLevel": "off",
    "validationAction": "warn"
});