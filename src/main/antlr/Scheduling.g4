grammar Scheduling;

LETTER: [A-Za-z0-9];
WS: [ \t\r\n]+ -> skip;
SINGLE_STRING: LETTER+;
DOUBLE_STRING: '"' ~('"')+ '"';

parameter_create: 'project' | 'classroom' | 'class' | 'lecturer';
method: 'create ' parameter_create;
query: method '(' feature+ ')';
string: LETTER | SINGLE_STRING | DOUBLE_STRING;
array: '[]' | '[' string ((',' string)+)? ']';
value: string | array;
feature: SINGLE_STRING ':' value ',';

kelas: LETTER | SINGLE_STRING;
constraint: 'constraint ' kelas kelas;