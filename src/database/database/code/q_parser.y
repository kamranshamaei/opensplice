%{
#include "os_stdlib.h"
#include "os_errno.h"
#include "c_base.h"
#include "c_collection.h"
#include "q_expr.h"
#include "q_helper.h"

#include "os_report.h"
#include "vortex_os.h"

/* yyscan_t is an opaque pointer, a typedef is required here to break a
   circular dependency introduced with bison 2.6 (normally a typedef is
   generated by flex). the define is required to disable the typedef in flex
   generated code */
#define YY_TYPEDEF_YY_SCANNER_T

typedef void *yyscan_t;

struct q_context {
    int line;
    int column;
    q_expr expr;
};


int
yyerror(
    yyscan_t yyscanner, struct q_context *context, char *text);

void
dollar_warning(
    void);
%}

%union {
    c_char *    String;
    c_char      Char;
    c_longlong  Integer;
    c_ulonglong UInteger;
    c_double    Float;
    c_type      Type;
    q_list      List;
    q_expr      Expr;
    q_tag       Tag;
}

%{
/* q_parser_lex must be declared here because q_parser_lex (yylex if no prefix
   would be specified) signature is augmented with an extra argument */
int q_parser_lex(
    YYSTYPE * yylval_param, yyscan_t yyscanner, struct q_context *context);

#define YY_DECL int q_parser_lex \
    (YYSTYPE * yylval_param, yyscan_t yyscanner, struct q_context *context)
%}

%define api.pure full
%define api.prefix q_parser_

%lex-param {yyscan_t scanner}
%lex-param {struct q_context *context}
%parse-param {yyscan_t scanner}
%parse-param {struct q_context *context}

%start program

/* Removed tokens because these are currently unused

%token PLUS MINUS DIV MOD ABS CONCAT QUERY
%token STRUCT SET BAG LIST ARRAY
%token REPAR LEPAR FIRST LAST IN EXISTS
%token UNIQUE SOME ANY COUNT SUM SUB MIN MAX AVG
%token DISTINCT FLATTEN SEMI DOUBLEDOT IMPORT
%token ORELSE FOR ANDTHEN QUOTE
%token IS_UNDEFINED IS_DEFINED UNSIGNED UNDEFINE
%token DATE ENUM TIME INTERVAL TIMESTAMP DICTIONARY
%token GROUP BY HAVING ORDER
%token ASC DESC INTERSECT UNION EXCEPT LISTTOSET ELEMENT
%token CTOKEN
*/

%token DEFINE AS_KEYWORD NIL TRUET FALSET LRPAR RRPAR
%token MUL LIKE BETWEEN
%token EQUAL NOTEQUAL GREATER LESS GREATEREQUAL LESSEQUAL
%token NOT AND OR REF
%token DOT ALL
%token SELECT UNDEFINED
%token FROM WHERE
%token COLON COMMA DOUBLECOLON
%token DOLLAR PERCENT
%token SELECT_DISTINCT JOIN

%token <String>   identifier stringLiteral queryId
%token <Char>     charLiteral
%token <Integer>  longlongLiteral
%token <UInteger> ulonglongLiteral
%token <Float>    doubleLiteral

%type  <List>    propertyList fieldList scopedName
%type  <Expr>    ID query projectionAttributes
                 fromClause relationalExpr
                 whereClauseOpt expr selectExpr
                 orExpr andExpr equalityExpr literal
                 postfixExpr joinExpr notExpr
                 objectConstruction field
%type  <Tag>     equalityOper relationalOper

%%

/***********************************************************************
 *
 * Query Program
 *
 ***********************************************************************/

program:
    | query
        { context->expr = F1(Q_EXPR_PROGRAM,$1); }
    ;

query:
      selectExpr
        { $$ = $1; }
    | expr
        { $$ = $1; }
    ;

/***********************************************************************
 *
 * Query Select expression
 *
 ***********************************************************************/

selectExpr:
      SELECT projectionAttributes fromClause whereClauseOpt
        { $$ = F3(Q_EXPR_SELECT,$2,$3,$4); }
    | SELECT_DISTINCT projectionAttributes fromClause whereClauseOpt
        { $$ = F3(Q_EXPR_SELECTDISTINCT,$2,$3,$4); }
    ;

projectionAttributes:
      MUL
        { $$ = L0(Q_EXPR_PROJECTION); }
    | postfixExpr
        { $$ = F1(Q_EXPR_PROJECTION,$1); }
    | fieldList
        { $$ = L1(Q_EXPR_PROJECTION,$1); }
    | objectConstruction
        { $$ = F1(Q_EXPR_PROJECTION,$1); }
    ;

objectConstruction:
      scopedName LRPAR fieldList RRPAR
        { $$ = L2(Q_EXPR_CLASS,q_newFnc(Q_EXPR_SCOPEDNAME,$1),$3); }
    ;

fieldList:
      field
        { $$ = List1($1); }
    | fieldList COMMA field
        { $$ = q_append($1,$3); }
    ;

field:
      postfixExpr COLON postfixExpr
        { $$ = F2(Q_EXPR_BIND,$3,$1); }
    | postfixExpr AS_KEYWORD postfixExpr
        { $$ = F2(Q_EXPR_BIND,$1,$3); }
    | postfixExpr
        { $$ = $1; }
    ;

/***********************************************************************
 *
 * Query Select From clause
 *
 ***********************************************************************/

fromClause:
      FROM joinExpr
        { $$ = F1(Q_EXPR_FROM,$2); }
    ;

joinExpr:
      ID
        { $$ = $1; }
    | ID JOIN joinExpr
        { $$ = F2(Q_EXPR_JOIN,$1,$3); }
    ;

/***********************************************************************
 *
 * Query Select Where clause
 *
 ***********************************************************************/

whereClauseOpt:
        { $$ = L0(Q_EXPR_WHERE); }
    | WHERE expr
        { $$ = F1(Q_EXPR_WHERE,$2); }
    ;

/***********************************************************************
 *
 * Query q_newFnc
 *
 ***********************************************************************/

expr:
      orExpr
        { $$ = $1; }
/*
    | NOT orExpr
        { $$ = F1(Q_EXPR_NOT,$2); }
*/
    ;

/***********************************************************************
 *
 * Query Boolean q_newFnc
 *
 ***********************************************************************/

orExpr:
      andExpr
        { $$ = $1; }
    | andExpr OR orExpr
        { $$ = F2(Q_EXPR_OR,$1,$3); }
    ;
/*
andExpr:
      equalityExpr
        { $$ = $1; }
    | equalityExpr AND andExpr
        { $$ = F2(Q_EXPR_AND,$1,$3); }
    ;
*/

andExpr:
      notExpr
        { $$ = $1; }
    | notExpr AND andExpr
        { $$ = F2(Q_EXPR_AND,$1,$3); }
    ;

notExpr:
      equalityExpr
        { $$ = $1; }
    | NOT notExpr
        { $$ = F1(Q_EXPR_NOT,$2); }
    ;

/***********************************************************************
 *
 * Query Comparisson q_newFnc
 *
 ***********************************************************************/

equalityExpr:
      relationalExpr
        { $$ = $1; }
    | postfixExpr equalityOper equalityExpr
        { $$ = F2($2,$1,$3); }
    | postfixExpr LIKE equalityExpr
        { $$ = F2(Q_EXPR_LIKE,$1,$3); }
    | LRPAR expr RRPAR
        { $$ = $2; }
    ;

relationalExpr:
      postfixExpr
        { $$ = $1; }
    | postfixExpr relationalOper relationalExpr
        { $$ = F2($2,$1,$3); }
    | ID BETWEEN postfixExpr AND postfixExpr
        { $$ = F2(Q_EXPR_AND,F2(Q_EXPR_GE,$1,$3),F2(Q_EXPR_LE,q_newId(q_getId($1)),$5)); }
    | ID NOT BETWEEN postfixExpr AND postfixExpr
        { $$ = F2(Q_EXPR_OR,F2(Q_EXPR_LT,$1,$4),F2(Q_EXPR_GT,q_newId(q_getId($1)),$6)); }
    ;

postfixExpr:
      ID
        { $$ = $1; }
    | ID propertyList
        { $$ = L2(Q_EXPR_PROPERTY,$1,$2); }
    | literal
        { $$ = $1; }
    | scopedName
        { $$ = q_newFnc(Q_EXPR_SCOPEDNAME,$1); }
    ;

propertyList:
      propertyOper ID
        { $$ = List1($2); }
    | propertyList propertyOper ID
        { $$ = q_append($1,$3); }
    ;

propertyOper:
      DOT
    | REF
    ;

ID:
      identifier
        { $$ = q_newId($1);
          /* frees dynamically malloced variable length string value
             allocated by the lexical scanner.
          */
          free($1);
        }
    ;

scopedName:
      identifier
        { $$ = List1(q_newId($1));
          /* frees dynamically malloced variable length string value
             allocated by the lexical scanner.
          */
          free($1);
        }
    | DOUBLECOLON identifier
        { $$ = List1(q_newId($2));
          /* frees dynamically malloced variable length string value
             allocated by the lexical scanner.
          */
          free($2);
        }
    | scopedName DOUBLECOLON identifier
        { $$ = q_append($1,q_newId($3));
          /* frees dynamically malloced variable length string value
             allocated by the lexical scanner.
          */
          free($3);
        }
    ;

equalityOper:
      EQUAL
        { $$ = Q_EXPR_EQ; }
    | NOTEQUAL
        { $$ = Q_EXPR_NE; }
    ;

relationalOper:
      LESS
        { $$ = Q_EXPR_LT; }
    | LESSEQUAL
        { $$ = Q_EXPR_LE; }
    | GREATER
        { $$ = Q_EXPR_GT; }
    | GREATEREQUAL
        { $$ = Q_EXPR_GE; }
    ;

literal:
      longlongLiteral
        { $$ = q_newInt($1); }
    | ulonglongLiteral
        { $$ = q_newUInt($1); }
    | doubleLiteral
        { $$ = q_newDbl($1); }
    | charLiteral
        { $$ = q_newChr($1); }
    | stringLiteral
        { $$ = q_newStr($1);
          /* frees dynamically malloced variable length string value
             allocated by the lexical scanner.
          */
          free($1);
        }
    | DOLLAR ulonglongLiteral
        { $$ = q_newVar($2);
          dollar_warning();
        }
    | PERCENT ulonglongLiteral
        { $$ = q_newVar($2); }
    | TRUET
        { $$ = q_newInt(TRUE); }
    | FALSET
        { $$ = q_newInt(FALSE); }
    | NIL
        { $$ = NULL; }
    | UNDEFINED
        { $$ = NULL; }
    ;

%%

#define YY_NO_UNPUT
#define YY_NO_INPUT

#include "q_parser.h" /* generated by flex */


int
yyerror(
    yyscan_t yyscanner, struct q_context *context, char *text)
{
    OS_UNUSED_ARG(yyscanner);

    OS_REPORT(OS_ERROR,"SQL parse failed",0,"%s near line: %d, column: %d",
              text, context->line, context->column);

    return 0;
}

void
dollar_warning(
    void)
{
    OS_REPORT(OS_WARNING,"SQL parser",0,"The use of '$' is deprecated, use '%%' instead");
}

q_expr
q_parse(
    const c_char *expression)
{
    YY_BUFFER_STATE buffer;
    yyscan_t scanner;
    struct q_context context = { 1, 0, NULL };
    q_expr e = NULL;

    if (expression) {
        q_parser_lex_init(&scanner);
        buffer = q_parser__scan_string(expression, scanner);
        if (yyparse(scanner, &context) == 0) {
            e = context.expr;
            q_exprSetText(e, expression);
        } else {
            /* 1 on invalid input, 2 on memory exhaustion */
            q_dispose(context.expr);
            context.expr = NULL;
        }

        q_parser__delete_buffer(buffer, scanner);
        q_parser_lex_destroy(scanner);
    }

    return e;
}
