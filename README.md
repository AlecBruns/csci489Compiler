Compiler Project for CSCI489


Language Grammar:

Extended BNF Grammar

Extended BNF Grammar

\<program\> ::= \[\<decl part\>\] \<st group\> ##
\<decl part\> ::= declare \<declare list\> enddeclare
\<decl list\> :: = \<decl\> {; \<decl\>}
\<decl\> ::= integer \<identifier list\>
\<st group\> ::= \<st\> {; \<st\>}
\<st\> ::= (\<asgn\> | \<read\> | \<write\> | \<cond\> | \<loop\>)
\<read\> ::= read \<identifier list\>
\<write\> ::= write \<output list\>
\<identifier list\> ::= \<identifier\> { , \<identifier\>}
\<output list\> ::= (\<expression\>{ , \<expression\>} | \<quote\> { , \<quote\>})
\<quote\> ::= ‘ \<word\>’
\<word\> ::= {(\<letter\>|\<digit\>)}
\<identifier\> ::= \<letter\> {(\<letter\>|\<digit\>)}
\<letter\> ::= (a | b | c | d | e | f | g | h | i | j | k | l | m | n | o | p | q |
                    r | s | t | u | v | w | x | y | z )
\<digit\> ::= (0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 )
\<const\> ::= \[-\] {\<digit\>}
\<asgn\> ::= \<identifier\> := \<expr\>
\<expr\> ::= \<term\> {(+|-) \<term\>}
\<term\> ::= \<factor\> {(* | /)\<factor\>}
\<factor\> ::= \[-\] \<factor2\>
\<factor2\> ::= (\<identifier\> | \<constant\> | ( \<expr\> ))
\<cond\> ::= \<if part\> \<st group\> \[else \<st group\>\] fi
\<if part\> ::= if \<expr\> \<rel\> \<expr\> then
\<rel\> ::= (= | \> | \< | \<= | \>+ | # | in)
\<loop\> := \<loop part\> loop \<st group\> endloop
\<loop part\> ::= to \<expr\> 

