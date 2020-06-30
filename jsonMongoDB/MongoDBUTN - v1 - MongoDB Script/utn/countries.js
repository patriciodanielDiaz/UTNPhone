use utn;

db.createCollection( "countries",{
    "storageEngine": {
        "wiredTiger": {}
    },
    "capped": false,
    "validator": {
        "$jsonSchema": {
            "bsonType": "object",
            "title": "countries",
            "additionalProperties": false,
            "properties": {
                "_id": {
                    "bsonType": "objectId"
                },
                "id": {
                    "bsonType": "objectId"
                },
                "country": {
                    "bsonType": "string"
                },
                "idprovinces": {
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