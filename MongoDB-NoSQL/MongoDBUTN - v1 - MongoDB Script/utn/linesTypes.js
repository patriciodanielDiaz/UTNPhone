use utn;

db.createCollection( "linesTypes",{
    "storageEngine": {
        "wiredTiger": {}
    },
    "capped": false,
    "validator": {
        "$jsonSchema": {
            "bsonType": "object",
            "title": "linesTypes",
            "additionalProperties": false,
            "properties": {
                "_id": {
                    "bsonType": "objectId"
                },
                "id": {
                    "bsonType": "objectId"
                },
                "type": {
                    "bsonType": "string",
                    "enum": [
                        "Celular",
                        "Telefonia Fija"
                    ]
                }
            }
        }
    },
    "validationLevel": "off",
    "validationAction": "warn"
});