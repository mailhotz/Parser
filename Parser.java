	 // parse token for <stmt>
    private void parseStmt(String token) throws IOException
    {
        int val;
		//System.out.println("Token from ln 52:" + token);
        if (token.equals("load"))             // ::= load "<string>"
        {
			if(!wasFalse){
				int i;
				String name = parseString(token);

				// interperter execution part
				line = loadPgm(name) + line;
			}
			else
			{
				token = getToken();
				token = getToken();
				token = getToken();
				wasFalse = false;
			}
        }
        else if (token.equals("print"))       // ::= print <expr>
        {
			if(!wasFalse){
				token = getToken();
				val = parseExpr(token);


				// interperter execution part
				System.out.println(val);
			}
			else
			{
				token = getToken();
				val = parseExpr(token);
				wasFalse = false;
			}
        }
        else if (token.equals("input"))       // ::= input <var>
        {
			if(!wasFalse)
			{
				token = getToken();
				String varToken = token;
			
				token = getToken();
				if(!token.equals("="))
					reportError(token);
			
				token = getToken();
				val = parseExpr(token);
			
				//Exeuction
				vars[varToken.charAt(0) - 97] = val;
			}
			else
			{
				token = getToken();
				token = getToken();
				if(!token.equals("="))
					reportError(token);
				token = getToken();
				val = parseExpr(token);
				wasFalse = false;
			}
        }
        else if (token.equals("if"))          // ::= if <cond> <stmt>
        {
			token = getToken();
			boolean isTrue; //Checks if true
			
			isTrue = parseCond(token);
			
			//execute <stmt>
			if(!wasFalse)
			{
				if(isTrue)
				{
					token = getToken();
					parseStmt(token);
				}
				else
					wasFalse = true;
			}
        }
		else if (token.equals("else"))
		{
			if(wasFalse){
				token = getToken();
				parseStmt(token);
			}
		}
        else if (isVar(token))                // ::= <var> = <expr>
        {
			if(!wasFalse)
			{
				String varToken = token;
			
				token = getToken();
				if(!token.equals("="))
					reportError(token);
				
				token = getToken();
				val = parseExpr(token);
			
				//Execution
				vars[varToken.charAt(0) - 97] = val;
			}
			else
			{
				token = getToken();
				if(!token.equals("="))
					reportError(token);
				token = getToken();
				val = parseExpr(token);
				wasFalse = false;
			}
        }
		else if(token.length() < 1)
		{
			//System.out.println("token is empty or neg length");
			getToken();
		}
        else
            reportError(token);
    }


	
	// parse token for <cond>
	private boolean parseCond(String token)
	{
		int val;
		String opToken;
		boolean retValue = false; //Return value
		
		val = parseVal(token);
		opToken = getToken();
		
		switch(opToken.charAt(0))
		{
			case '>':						 // ::= <val> > <val>
				if(val > parseVal(getToken()))
					retValue = true;
				break;
			case '<':						 // ::= <val> < <val>
				if(val < parseVal(getToken()))
					retValue = true;
				break;
			case '=':						 // ::= <val> == <val>
				opToken = getToken();
				
				if(!opToken.equals("="))
					reportError(opToken);
					
				opToken = getToken();
				
				if(val == parseVal(opToken))
					retValue = true;

				break;
		}
		
		return retValue;
	}