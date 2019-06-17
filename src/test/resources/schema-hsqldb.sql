CREATE TABLE IF NOT EXISTS users(
					id 		  			BIGINT 		  NOT NULL		IDENTITY, 
					username 			VARCHAR(30)   NOT NULL,
					password     VARCHAR(100)   NOT NULL,
					enabled      SMALLINT 		  NOT NULL,
					firstName		VARCHAR(50),
					lastName		VARCHAR(50)
					
					);
					
CREATE TABLE IF NOT EXISTS authorities(
					id 		  			BIGINT 		  NOT NULL		IDENTITY, 
					username 			VARCHAR(30)   NOT NULL,
					authority     VARCHAR(100)   NOT NULL
					
					);
					
					

					