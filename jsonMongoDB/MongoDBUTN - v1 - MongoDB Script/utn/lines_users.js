use utn;

db.createCollection( "lines_users",{
    "storageEngine": {
        "wiredTiger": {}
    },
    "capped": false,
    "validator": {
        "$jsonSchema": {
            "bsonType": "object",
            "title": "lines_users",
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