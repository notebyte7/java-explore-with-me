CREATE TABLE IF NOT EXISTS users (
    id 		    		BIGINT			                GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name 				VARCHAR(250)	                NOT NULL,
    email 				VARCHAR(254) 	                UNIQUE NOT NULL
    );

CREATE TABLE IF NOT EXISTS categories (
    id 		    		BIGINT			                GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name 				VARCHAR(50)		                UNIQUE NOT NULL
    );

CREATE TABLE IF NOT EXISTS locations (
    id 		    		BIGINT			                GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    lat 				NUMERIC(8,6)	                NOT NULL,
    lon 				NUMERIC(9,6) 	                NOT NULL,
    UNIQUE(lat, lon)
    );

CREATE TABLE IF NOT EXISTS events (
    event_id 		    BIGINT			                GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    initiator 			BIGINT		                    REFERENCES users (id) ON DELETE CASCADE,
    title 				VARCHAR(120) 	                NOT NULL,
    annotation			VARCHAR(2000)	                NOT NULL,
    category    		BIGINT			                REFERENCES categories (id) ON DELETE CASCADE,
    description 		VARCHAR(7000)	                NOT NULL,
    event_date			TIMESTAMP WITHOUT TIME ZONE		NOT NULL CHECK (event_date >= NOW() + INTERVAL '2' HOUR),
    created_on			TIMESTAMP WITHOUT TIME ZONE		NOT NULL DEFAULT NOW(),
    published_on		TIMESTAMP WITHOUT TIME ZONE		,
    location			BIGINT		                    REFERENCES locations (id) ON DELETE CASCADE,
    participant_limit	BIGINT			                NOT NULL DEFAULT 0,
    paid				BOOLEAN			                NOT NULL DEFAULT FALSE,
    request_moderation	BOOLEAN			                NOT NULL DEFAULT TRUE,
    state				VARCHAR(20)		                NOT NULL DEFAULT 'PENDING'
    );

CREATE TABLE IF NOT EXISTS participation_requests (
    id 		    		BIGINT			                GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event 				BIGINT		                    REFERENCES events (event_id) ON DELETE CASCADE,
    requester			BIGINT		                    REFERENCES users (id) ON DELETE CASCADE,
    created				TIMESTAMP WITHOUT TIME ZONE		NOT NULL DEFAULT NOW(),
    status				VARCHAR(20)		                NOT NULL DEFAULT 'PENDING'
    );

CREATE TABLE IF NOT EXISTS compilations (
    compilation_id 		BIGINT			                GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title 				VARCHAR(50)		                NOT NULL,
    pinned				BOOLEAN			                NOT NULL DEFAULT FALSE
    );

CREATE TABLE IF NOT EXISTS events_compilations (
    compilation_id 		BIGINT			                REFERENCES compilations (compilation_id) ON DELETE CASCADE,
    event_id 			BIGINT		                    REFERENCES events (event_id) ON DELETE CASCADE
    );

