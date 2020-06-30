use utn;

db.createCollection( "calls",{
    "storageEngine": {
        "wiredTiger": {}
    },
    "capped": false,
    "validator": {
        "$jsonSchema": {
            "bsonType": "object",
            "title": "calls",
            "additionalProperties": false,
            "properties": {
                "_id": {
                    "bsonType": "objectId"
                },
                "id": {
                    "bsonType": "objectId"
                },
                "origincall": {
                    "bsonType": "objectId"
                },
                "destinationcall": {
                    "bsonType": "objectId"
                },
                "duration": {
                    "bsonType": "number",
                    "description": "tiempo medido en minutos\n"
                },
                "price": {
                    "bsonType": "double"
                },
                "total": {
                    "bsonType": "double"
                },
                "idrate": {
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