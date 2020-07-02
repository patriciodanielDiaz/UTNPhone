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
                "destinationcall": {
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
                "duration": {
                    "bsonType": "number",
                    "description": "tiempo medido en minutos\n"
                },
                "total": {
                    "bsonType": "double"
                },
                "rate": {
                    "bsonType": "array",
                    "additionalItems": true,
                    "uniqueItems": false,
                    "items": [
                        {
                            "bsonType": "objectId"
                        },
                        {
                            "bsonType": "double"
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
                        },
                        {
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
                    ]
                }
            }
        }
    },
    "validationLevel": "off",
    "validationAction": "warn"
});