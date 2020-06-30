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
                "iduser": {
                    "bsonType": "objectId"
                }
            },
            "required": [
                "id"
            ]
        }
    },
    "validationLevel": "off",
    "validationAction": "warn"
});