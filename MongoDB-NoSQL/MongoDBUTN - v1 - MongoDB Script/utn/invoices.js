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
                "user": {
                    "bsonType": "array",
                    "additionalItems": true,
                    "uniqueItems": false,
                    "items": [
                        {
                            "bsonType": "objectId"
                        },
                        {
                            "bsonType": "string"
                        },
                        {
                            "bsonType": "string"
                        },
                        {
                            "bsonType": "string"
                        }
                    ]
                },
                "line": {
                    "bsonType": "array",
                    "additionalItems": true,
                    "uniqueItems": false,
                    "items": [
                        {
                            "bsonType": "objectId"
                        },
                        {
                            "bsonType": "string",
                            "description": "number"
                        }
                    ]
                },
                "total": {
                    "bsonType": "double"
                },
                "expirationdate": {
                    "bsonType": "date"
                },
                "date_issued": {
                    "bsonType": "date"
                },
                "state": {
                    "bsonType": "string",
                    "enum": [
                        "pagada",
                        "impaga"
                    ]
                }
            },
            "dependencies": {
                "id": [
                    "user"
                ]
            }
        }
    },
    "validationLevel": "off",
    "validationAction": "warn",
    "collation": {
        "locale": "en_US",
        "strength": 3,
        "caseFirst": "off",
        "alternate": "non-ignorable",
        "backwards": true
    }
});