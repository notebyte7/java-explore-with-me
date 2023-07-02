CREATE TABLE IF NOT EXISTS endpoint_hits (
    id 		    BIGINT			                GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    app 		VARCHAR(50)		                NOT NULL,
    uri 		VARCHAR 	                    NOT NULL,
    ip 		    VARCHAR(50)		                NOT NULL,
    timestamp   TIMESTAMP WITHOUT TIME ZONE     NOT NULL
    );