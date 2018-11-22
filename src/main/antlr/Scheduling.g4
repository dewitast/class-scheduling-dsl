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

class_string: LETTER | SINGLE_STRING;
constraint: 'add constraint' constraint_type;
schedule: SINGLE_STRING;
unavailability: '[' schedule ((',' schedule)+)? ']';
lecturer: LETTER | SINGLE_STRING | DOUBLE_STRING;
constraint_type: 'class' class_string class_string | 'lecture unavailability' lecturer unavailability |
    'restricted hour' schedule;