# An Authorization Server for AH Users

This Spring Boot authorization server authenticates AH users and, upon successful authentication, returns a JWT that can then be used to access AH resources. Since the token is RSA-encrypted, the server will return its public key as a JWK set if requested.


As of version 1.0.0-RELEASE, this application is live at `https://guarded-crag-86541.herokuapp.com`.

What follows is a basic rundown of the server's endpoints.


### `GET /.well-known/jwks.json` 

GET the JWK set with the public keys to decrypt the JWT.
```
{
	"keys" : [
		{
			"kid": "...",
			"p": "...",
			"kty": "...",
   		"q": "...",
   		"d": "...",
   		"e": "...",
   		"use" : "...",
   		"qi" : "...",
   		"dp" : "...",
   		"dq" : "...",
   		"dn" : "..."		
   	},
   
   	{ ... }
 	   
   	]
   
}  
	  
```
... and `200 OK`.


### `POST /token`

POSTing to this endpoint ignores the body, and returns the token of the authenticated user
```
{
	"token" : "xxx.yyy.zzz"
}
```

The JWT contains at least the following claims:
- `scope`: each authority is separated by an empty space (`"ROLE_ADMIN ROLE_USER ..."`)
- `exp`: the token expires in 1 hour



## Security
Authentication is only required to obtain a JWT. Basic authentication is the authentication mechanism.



