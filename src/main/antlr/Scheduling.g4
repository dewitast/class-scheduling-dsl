grammar Scheduling;

LETTER: [A-Za-z0-9];
WS: [ \t\r\n]+ -> skip;
SINGLE_STRING: (LETTER|'-')+;
DOUBLE_STRING: '"' ~('"')+ '"';

target: 'classroom' | 'class' | 'lecturer';
function:'create'|'show'|'update'|'delete';
target_key: LETTER | SINGLE_STRING | DOUBLE_STRING;
update_key: 'add'|'remove';
query: function target (target_key)? (update_key)? ('(' feature+ ')')?;
string: LETTER | SINGLE_STRING | DOUBLE_STRING;
array: '[]' | '[' string ((',' string)+)? ']';
value: string | array;
feature: SINGLE_STRING ':' value ',';


constraint: 'add constraint' constraint_type;
class_string: LETTER | SINGLE_STRING;
schedule: SINGLE_STRING;
unavailability: '[' schedule ((',' schedule)+)? ']';
lecturer: LETTER | SINGLE_STRING | DOUBLE_STRING;
constraint_type: 'class' class_string class_string | 'lecture unavailability' lecturer unavailability |
    'restricted hour' schedule;

preferences: '[' schedule ((',' schedule)+)? ']';
preference: 'add preference' lecturer preferences;