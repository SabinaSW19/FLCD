
%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define YYDEBUG 1
%}

%verbose
%error-verbose

%token IDENTIFIER
%token CONSTANT
%token INT
%token CHAR
%token IF
%token ELSE
%token WHILE
%token INPUT
%token PRINT
%token BEGINN
%token END
%token LEFT_ROUND
%token RIGHT_ROUND
%token LEFT_SQUARE
%token RIGHT_SQUARE
%token COMMA
%token SEMI_COLON
%token PLUS
%token MINUS
%token MULTIPLY
%token DIVIDE
%token MODULO
%token LESS
%token LESS_OR_EQUAL
%token SIMPLE_EQUAL
%token GREATER_OR_EQUAL
%token GREATER
%token DIFFERENT
%token DOUBLE_EQUAL

%start program

%%

program : BEGINN stmtlist END
	;
declaration : type IDENTIFIER SEMI_COLON 
	;
type1 : INT | CHAR 
	;
arraydecl : type LEFT_SQUARE RIGHT_SQUARE
	;
type : type1 | arraydecl
	;
cmpdstmt : BEGIN stmtlist END
	;
stmtlist : stmt | stmt stmtlist
	;
stmt : declaration | simplestmt | structstmt
	;
simplestmt : assignstmt | iostmt
	;
assignstmt : IDENTIFIER SIMPLE_EQUAL expression SEMI_COLON
	;
expression : term | expression operation expression | LEFT_ROUND expression operation expression RIGHT_ROUND
	;
operation : PLUS | MINUS | MULTIPLY | DIVIDE | MODULO
	;
term : IDENTIFIER | CONSTANT
	;
iostmt : INPUT LEFT_ROUND IDENTIFIER RIGHT_ROUND SEMI_COLON | PRINT LEFT_ROUND IDENTIFIER RIGHT_ROUND SEMI_COLON | PRINT LEFT_ROUND CONSTANT RIGHT_ROUND SEMI_COLON
	;
structstmt : ifstmt | whilestmt
	;
ifstmt : IF LEFT_ROUND condition RIGHT_ROUND cmpdstmt | IF LEFT_ROUND condition RIGHT_ROUND cmpdstmt ELSE cmpdstmt
	;
whilestmt : WHILE LEFT_ROUND condition RIGHT_ROUND cmpdstmt
	;
condition : expression relation expression
	;
relation : LESS | LESS_OR_EQUAL | DOUBLE_EQUAL | DIFFERENT | GREATER_OR_EQUAL | GREATER 
	;


%%
yyerror(char *s)
{
  printf("%s\n", s);
}

extern FILE *yyin;

main(int argc, char **argv)
{
  if (argc > 1)
    yyin = fopen(argv[1], "r");
  if ( (argc > 2) && ( !strcmp(argv[2], "-d") ) )
    yydebug = 1;
  if ( !yyparse() )
    fprintf(stderr,"\t WORKING !!!\n");
}