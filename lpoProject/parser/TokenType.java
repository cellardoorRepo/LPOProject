package lpoProject.parser;

public enum TokenType { // important: IDENT, NUM, and SKIP must have ordinals 1, 2 and 3
	EOF, IDENT, NUM, SKIP, IN, FOR, BOOL, PRINT, PREFIX, VAR, PLUS, TIMES, ASSIGN, OPEN_PAR, CLOSE_PAR, STMT_SEP, EXP_SEP, OPEN_BLOCK, CLOSE_BLOCK, MINUS, OPEN_LIST, CLOSE_LIST, AND, EQ, NOT, IF, ELSE, DO, WHILE, OPT
}
