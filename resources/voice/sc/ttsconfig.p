﻿% for turbo-prolog
:- op('--', xfy, 500).
% for swi-prolog
:- op(500, xfy,'--').

version(103).
tts :- version(X), X > 99.
voice :- version(X), X < 99.

language('sc').
fest_language('Sardinian').


% IMPLEMENTED (X) or MISSING ( ) FEATURES:
% (X) new Version 1.5 format
% (X) route calculated prompts, left/right, u-turns, roundabouts, straight/follow
% (X) arrival
% (X) other prompts: attention (without Type implementation), location lost, off_route, exceed speed limit
% (X) attention Type implementation
% (X) special grammar: onto / on / to Street fur turn and follow commands
% (X) special grammar: nominative/dativ for distance measure
% (X) special grammar: imperative/infinitive distincion for turns
% (X) distance measure: meters / feet / yard support
% (X) Street name announcement (suppress in prepare_roundabout)
% (X) Name announcement for destination / intermediate / GPX waypoint arrival
% (X) Time announcement for new and recalculated route (for recalculated suppress in appMode=car)
% (X) word order checked
% (X) Announcement of favorites, waypoints and pois along the route
% (X) Support announcement of railroad crossings and pedestrian crosswalks


% ROUTE CALCULATED
string('route_is1.ogg', 'Su biàgiu est ').
string('route_is2.ogg', 'longu ').
string('route_calculate.ogg', 'Ricàlculu àndala').
string('distance.ogg', 'distàntzia ').


% LEFT/RIGHT
string('prepare.ogg', 'Preparade·bos a ').
string('after.ogg', 'pustis de ').

string('left.ogg', 'bortare a manca').
string('left_sh.ogg', 'luego a manca').
string('left_sl.ogg', 'bortare de pagu a manca').
string('right.ogg', 'bortare a destra').
string('right_sh.ogg', 'luego a destra').
string('right_sl.ogg', 'bortare de pagu a destra').
string('left_keep.ogg', 'mantènnere sa manca').
string('right_keep.ogg', 'mantènnere sa destra').
% if needed, "left/right_bear.ogg" can be defined here also. "... (then) (bear_left/right)" is used in pre-announcements to indicate the direction of a successive turn AFTER the next turn.

% U-TURNS
string('make_uturn1.ogg', 'faghide una furriadura a u ').
string('make_uturn2.ogg', 'Preparade·bos a una furriadura a U ').
string('make_uturn_wp.ogg', 'In su primu momentu possìbile, fàghere una furriada a u').

% ROUNDABOUTS
string('prepare_roundabout.ogg', 'a intrare in una rutunda').
string('roundabout.ogg', 'intrade in sa rutunda, ').
string('then.ogg', ', pustis').
string('and.ogg', 'e').
string('take.ogg', 'pigade sa').
string('exit.ogg', 'essida').

string('1st.ogg', 'prima').
string('2nd.ogg', 'segunda').
string('3rd.ogg', 'terza').
string('4th.ogg', 'cuarta').
string('5th.ogg', 'cuinta').
string('6th.ogg', 'sesta').
string('7th.ogg', 'sètima').
string('8th.ogg', 'otava').
string('9th.ogg', 'nona').
string('10th.ogg', 'dètzima').
string('11th.ogg', 'undighèsima').
string('12th.ogg', 'doighèsima').
string('13th.ogg', 'treighèsima').
string('14th.ogg', 'batordighèsima').
string('15th.ogg', 'bindighèsima').
string('16th.ogg', 'seighèsima').
string('17th.ogg', 'deghesetèsima').

% STRAIGHT/FOLLOW
string('go_ahead.ogg', 'Sighide deretu').
string('follow.ogg', 'Sighide pro').

% ARRIVE
string('and_arrive_destination.ogg', 'e ais a arribare a destinatzione').
string('reached_destination.ogg','arribadu a destinatzione').
string('and_arrive_intermediate.ogg', 'e ais a arribare a su puntu mesanu').
string('reached_intermediate.ogg', 'arrivati al punto intermedio').
string('and_arrive_waypoint.ogg', 'arribadu a su puntu mesanu').
string('reached_waypoint.ogg', 'arribadu a su puntu mesanu').

%NEARBY POINTS
string('and_arrive_waypoint.ogg', 'e arribades a su puntu mesanu ').
string('reached_waypoint.ogg', 'arribadu a su puntu mesanu ').
string('and_arrive_favorite.ogg', 'e arriverai al preferito ').
string('reached_favorite.ogg', 'arribadu a su prefèrridu ').
string('and_arrive_poi.ogg', 'e ais a arribare a su puntu de interesse ').
string('reached_poi.ogg', 'arribadu a su puntu de interesse').

% OTHER PROMPTS
string('attention.ogg', 'atentzione, ').
string('speed_camera.ogg', 'Autovelox ').
string('border_control.ogg', 'Dogana ').
string('railroad_crossing.ogg', 'Coladòrgiu a livellu ').
string('traffic_calming.ogg', 'Minimadore de lestresa ').
string('toll_booth.ogg', 'Casellu ').
string('stop.ogg', 'Stop ').
string('pedestrian_crosswalk.ogg', 'Atraessamentu pedonale ').
string('location_lost.ogg', 'Sinnale GPS pèrdidu').
string('location_recovered.ogg', 'Sinnale GPS torradu ').
string('off_route.ogg', 'Seis essidos dae s'àndala').
string('exceed_limit.ogg', 'Barigadu su lìmite de lestresa').

% STREET NAME GRAMMAR
string('onto.ogg', 'in ').
string('on.ogg', 'in ').
string('to.ogg', 'pro ').
string('with.ogg', 'pro ').  % is used if you turn together with your current street, i.e. street name does not change.
string('toward.ogg', 'pro ').
 
% Utility: toLowerCaseStr(OldString,NewString)
toLowerCaseStr(L1,L1):-  var(L1), !.
toLowerCaseStr([],[]):-  !.
toLowerCaseStr([H1|T1],[H2|T2]):- H1>64,H1<91, !, H2 is H1+32, toLowerCaseStr(T1,T2).
toLowerCaseStr([H1|T1],[H1|T2]):- toLowerCaseStr(T1,T2).

% Utility: toLowerCaseAto(OldString,NewString)
toLowerCaseAto(A1,A2) :- atom_codes(A1,S1),toLowerCaseStr(S1,S2),atom_codes(A2,S2).

% Utility: removeSemicolonStr(OldString,NewString)
removeSemicolonStr(L1,L1):-  var(L1), !.
removeSemicolonStr([],[]):-  !.
removeSemicolonStr([H1|T1],[H2|T2]):- H1=59, !, H2 is 32, removeSemicolonStr(T1,T2).
removeSemicolonStr([H1|T1],[H1|T2]):- removeSemicolonStr(T1,T2).

% Utility: removeSemicolonAto(OldString,NewString)
removeSemicolonAto(A1,A2) :- atom_codes(A1,S1),removeSemicolonStr(S1,S2),atom_codes(A2,S2).

% Utility: reverseStr(OldStr,[],RevStr)
reverseStr([H|T], A, R) :- reverseStr(T, [H|A], R).
reverseStr([], A, A).

% Utility: startsWithStr(String,Match)
startsWithStr([],[]).
startsWithStr([H1|T1],[]):- startsWithStr(T1,[]).
startsWithStr([H1|T1],[H1|T2]):- startsWithStr(T1,T2).

% Utility endsWithString(String, Match)
endsWithString(A1,A2) :- atom_codes(A1,S1),atom_codes(A2,S2),reverseStr(S1,[],R1),reverseStr(S2,[],R2),toLowerCaseStr(R1,RL1),startsWithStr(RL1,R2).

isMale(Street) :-  endsWithString(Street, 'via').
isMale(Street) :-  endsWithString(Street, 'rutunda').
isMale(Street) :-  endsWithString(Street, 'damm'). % da controllare e tradurre 
isMale(Street) :-  endsWithString(Street, 'pratza').
isMale(Street) :-  endsWithString(Street, 'mercadu').
isMale(Street) :-  endsWithString(Street, 'martzapede').
isMale(Street) :-  endsWithString(Street, 'caminera').

isFemale(Street) :-  endsWithString(Street, 'istrada').
isFemale(Street) :-  endsWithString(Street, 'istrada').
isFemale(Street) :-  endsWithString(Street, 'autoistrada').
isFemale(Street) :-  endsWithString(Street, 'chaussee'). % da tradurre
isFemale(Street) :-  endsWithString(Street, 'gùturu').
isFemale(Street) :-  endsWithString(Street, 'zeile'). % da tradurre
isFemale(Street) :-  endsWithString(Street, 'viale').
isFemale(Street) :-  endsWithString(Street, '0').
isFemale(Street) :-  endsWithString(Street, '1').
isFemale(Street) :-  endsWithString(Street, '2').
isFemale(Street) :-  endsWithString(Street, '3').
isFemale(Street) :-  endsWithString(Street, '4').
isFemale(Street) :-  endsWithString(Street, '5').
isFemale(Street) :-  endsWithString(Street, '6').
isFemale(Street) :-  endsWithString(Street, '7').
isFemale(Street) :-  endsWithString(Street, '8').
isFemale(Street) :-  endsWithString(Street, '9').

street_is_male(voice([Ref, Name, Dest],_)) :- isMale(Name).
street_is_female(voice([Ref, Name, Dest],_)) :- isFemale(Name).
street_is_female(voice([Ref, Name, Dest],_)) :- isFemale(Ref).
street_is_nothing(voice([Ref, Name, Dest],_)) :- not(isMale(Name)), not(isFemale(Name)).

% DISTANCE UNIT SUPPORT
string('meters_nominativ.ogg', 'metros ').
string('meters_dativ.ogg', 'metros ').
string('around_1_kilometer_nominativ.ogg', 'pagu prus o mancu unu chilòmetru ').
string('around_1_kilometer_dativ.ogg', 'pagu prus o mancu unu chilòmetru ').
string('around.ogg', 'pagu prus o mancu ').
string('kilometers_nominativ.ogg', 'chilòmetros ').
string('kilometers_dativ.ogg', 'chilòmetros ').

string('feet_nominativ.ogg', 'pedes ').
string('feet_dativ.ogg', 'pedes ').
string('1_tenth_of_a_mile_nominativ.ogg', 'unu dètzimu de mìgliu ').
string('1_tenth_of_a_mile_dativ.ogg', 'unu dètzimu de mìgliu ').
string('tenths_of_a_mile_nominativ.ogg', 'detzimos de mìgliu ').
string('tenths_of_a_mile_dativ.ogg', 'detzimos de mìgliu ').
string('around_1_mile_nominativ.ogg', 'circa un miglio ').
string('around_1_mile_dativ.ogg', 'circa un miglio ').
string('miles_nominativ.ogg', 'mìglios').
string('miles_dativ.ogg', 'mìglios ').

string('yards_nominativ.ogg', 'iardas ').
string('yards_dativ.ogg', 'iardas ').

% TIME SUPPORT
string('time.ogg', 'tempus netzessàriu ').
string('1_hour.ogg', 'un ora ').
string('hours.ogg', 'oras ').
string('less_a_minute.ogg', 'de mancu de unu minutu ').
string('1_minute.ogg', 'unu minutu ').
string('minutes.ogg', 'minutos ').

%% COMMAND BUILDING / WORD ORDER
turn('left', ['left.ogg']).
turn('left_sh', ['left_sh.ogg']).
turn('left_sl', ['left_sl.ogg']).
turn('right', ['right.ogg']).
turn('right_sh', ['right_sh.ogg']).
turn('right_sl', ['right_sl.ogg']).
turn('left_keep', ['left_keep.ogg']).
turn('right_keep', ['right_keep.ogg']).
bear_left(_Street) -- ['left_keep.ogg'].
bear_right(_Street) -- ['right_keep.ogg'].

% cut_part_street(voice([Ref, Name, Dest], [_CurrentRef, _CurrentName, _CurrentDest]), _).
% cut_part_street(voice(['', Name, _], _), Name). % not necessary
% Next 2 lines for Name taking precedence over Dest...
%cut_part_street(voice([Ref, '', Dest], _), [C1, 'toward.ogg', Dest]) :- atom_concat(Ref, ' ', C1).
%cut_part_street(voice([Ref, Name, _], _), Concat) :- atom_concat(Ref, ' ', C1), atom_concat(C1, Name, Concat).
cut_part_street(voice([Ref, Name, _], _), Concat) :- atom_concat(Name, ' ', C1), atom_concat(C1, Ref, Concat).
% ...or next 2 lines for Dest taking precedence over Name
%cut_part_street(voice([Ref, Name, ''], _), Concat) :- atom_concat(Ref, ' ', C1), atom_concat(C1, Name, Concat).
%cut_part_street(voice([Ref, _, Dest], _), [C1, 'toward.ogg', Dest]) :- atom_concat(Ref, ' ', C1).


turn_street('', []).
turn_street(voice(['','',''],_), []).
turn_street(voice(['', '', D], _), ['toward.ogg', DestClean]) :- tts, removeSemicolonAto(D,DestClean).
turn_street(Street, ['onto.ogg', SName]) :- tts, not(Street = voice(['', '', D], _)), street_is_male(Street), cut_part_street(Street, SName).
turn_street(Street, ['onto.ogg', SName]) :- tts, not(Street = voice(['', '', D], _)), street_is_female(Street), cut_part_street(Street, SName).
turn_street(Street, ['onto.ogg', SName]) :- tts, not(Street = voice(['', '', D], _)), street_is_nothing(Street), cut_part_street(Street, SName).
turn_street(_Street, []) :- not(tts).

follow_street('', []).
follow_street(voice(['','',''],_), []).
follow_street(voice(['', '', D], _), ['to.ogg', D]) :- tts.
follow_street(Street, ['to.ogg', SName]) :- tts, not(Street = voice([R, S, _],[R, S, _])), street_is_male(Street), cut_part_street(Street, SName).
follow_street(Street, ['to.ogg', SName]) :- tts, not(Street = voice([R, S, _],[R, S, _])), street_is_female(Street), cut_part_street(Street, SName).
follow_street(Street, ['to.ogg', SName]) :- tts, not(Street = voice([R, S, _],[R, S, _])), street_is_nothing(Street), cut_part_street(Street, SName).
follow_street(Street, ['on.ogg', SName]) :- tts, Street = voice([R, S, _],[R, S, _]), cut_part_street(Street, SName).
follow_street(_Street, []) :- not(tts).

prepare_turn(Turn, Dist, Street) -- ['prepare.ogg', 'after.ogg', D, M | Sgen] :- distance(Dist, dativ) -- D, turn(Turn, M), turn_street(Street, Sgen).
turn(Turn, Dist, Street) -- ['after.ogg', D, M, ' '| Sgen] :- distance(Dist, dativ) -- D, turn(Turn, M), turn_street(Street, Sgen).
turn(Turn, Street) -- [M, ' '| Sgen] :- turn(Turn, M), turn_street(Street, Sgen).

prepare_make_ut(Dist, Street) -- ['prepare.ogg', 'after.ogg', D, 'make_uturn2.ogg' | Sgen] :- distance(Dist, dativ) -- D, turn_street(Street, Sgen).
make_ut(Dist, Street) --  ['after.ogg', D, 'make_uturn1.ogg' | Sgen] :- distance(Dist, dativ) -- D, turn_street(Street, Sgen).
make_ut(Street) -- ['make_uturn2.ogg' | Sgen] :- turn_street(Street, Sgen).
make_ut_wp -- ['make_uturn_wp.ogg'].

prepare_roundabout(Dist, _Exit, _Street) -- ['prepare.ogg', 'after.ogg', D, 'prepare_roundabout.ogg'] :- distance(Dist, dativ) -- D.
roundabout(Dist, _Angle, Exit, Street) -- ['after.ogg', D, 'roundabout.ogg', 'then.ogg', 'take.ogg', E, 'exit.ogg' | Sgen] :- distance(Dist, dativ) -- D, nth(Exit, E), turn_street(Street, Sgen).
roundabout(_Angle, Exit, Street) -- ['take.ogg', E, 'exit.ogg' | Sgen] :- nth(Exit, E), turn_street(Street, Sgen).

go_ahead -- ['go_ahead.ogg'].
go_ahead(Dist, Street) -- ['follow.ogg', D | Sgen]:- distance(Dist, nominativ) -- D, follow_street(Street, Sgen).

then -- ['then.ogg'].
name(D, [D]) :- tts.
name(_D, []) :- not(tts).
and_arrive_destination(D) -- ['and_arrive_destination.ogg', Ds] :- name(D, Ds).
reached_destination(D) -- ['reached_destination.ogg', Ds] :- name(D, Ds).
and_arrive_intermediate(D) -- ['and_arrive_intermediate.ogg', Ds] :- name(D, Ds).
reached_intermediate(D) -- ['reached_intermediate.ogg', Ds] :- name(D, Ds).
and_arrive_waypoint(D) -- ['and_arrive_waypoint.ogg'|Ds] :- name(D, Ds).
reached_waypoint(D) -- ['reached_waypoint.ogg'|Ds] :- name(D, Ds).
and_arrive_favorite(D) -- ['and_arrive_favorite.ogg'|Ds] :- name(D, Ds).
reached_favorite(D) -- ['reached_favorite.ogg'|Ds] :- name(D, Ds).
and_arrive_poi(D) -- ['and_arrive_poi.ogg'|Ds] :- name(D, Ds).
reached_poi(D) -- ['reached_poi.ogg'|Ds] :- name(D, Ds).
 
route_new_calc(Dist, Time) -- ['route_is1.ogg', 'route_is2.ogg', D, 'time.ogg', T] :- distance(Dist, nominativ) -- D, time(Time) -- T.
route_recalc(_Dist, _Time) -- ['route_calculate.ogg'] :- appMode('car').
route_recalc(Dist, Time) -- ['route_calculate.ogg', 'distance.ogg', D, 'time.ogg', T] :- distance(Dist, nominativ) -- D, time(Time) -- T.
 
location_lost -- ['location_lost.ogg'].
location_recovered -- ['location_recovered.ogg'].
off_route(Dist) -- ['off_route.ogg', D] :- distance(Dist, dativ) -- D.
speed_alarm -- ['exceed_limit.ogg'].
% attention(_Type) -- ['attention.ogg'].
attention(Type) -- ['attention.ogg', W] :- warning(Type, W).

% TRAFFIC WARNINGS
warning('SPEED_CAMERA', 'speed_camera.ogg').
warning('SPEED_LIMIT', '').
warning('BORDER_CONTROL', 'border_control.ogg').
warning('RAILWAY', 'railroad_crossing.ogg').
warning('TRAFFIC_CALMING', 'traffic_calming.ogg').
warning('TOLL_BOOTH', 'toll_booth.ogg').
warning('STOP', 'stop.ogg').
warning('PEDESTRIAN', 'pedestrian_crosswalk.ogg').
warning('MAXIMUM', '').
warning(Type, '') :- not(Type = 'SPEED_CAMERA'; Type = 'SPEED_LIMIT'; Type = 'BORDER_CONTROL'; Type = 'RAILWAY'; Type = 'TRAFFIC_CALMING'; Type = 'TOLL_BOOTH'; Type = 'STOP'; Type = 'PEDESTRIAN'; Type = 'MAXIMUM').

%% 
nth(1, '1st.ogg').
nth(2, '2nd.ogg').
nth(3, '3rd.ogg').
nth(4, '4th.ogg').
nth(5, '5th.ogg').
nth(6, '6th.ogg').
nth(7, '7th.ogg').
nth(8, '8th.ogg').
nth(9, '9th.ogg').
nth(10, '10th.ogg').
nth(11, '11th.ogg').
nth(12, '12th.ogg').
nth(13, '13th.ogg').
nth(14, '14th.ogg').
nth(15, '15th.ogg').
nth(16, '16th.ogg').
nth(17, '17th.ogg').


%% resolve command main method
%% if you are familar with Prolog you can input specific to the whole mechanism,
%% by adding exception cases.
flatten(X, Y) :- flatten(X, [], Y), !.
flatten([], Acc, Acc).
flatten([X|Y], Acc, Res):- flatten(Y, Acc, R), flatten(X, R, Res).
flatten(X, Acc, [X|Acc]) :- version(J), J < 100, !.
flatten(X, Acc, [Y|Acc]) :- string(X, Y), !.
flatten(X, Acc, [X|Acc]).

resolve(X, Y) :- resolve_impl(X,Z), flatten(Z, Y).
resolve_impl([],[]).
resolve_impl([X|Rest], List) :- resolve_impl(Rest, Tail), ((X -- L) -> append(L, Tail, List); List = Tail).


% handling alternatives
[X|_Y] -- T :- (X -- T),!.
[_X|Y] -- T :- (Y -- T).


pnumber(X, Y) :- tts, !, num_atom(X, Y).
pnumber(X, Ogg) :- num_atom(X, A), atom_concat(A, '.ogg', Ogg).
% time measure
hours(S, []) :- S < 60.
hours(S, ['1_hour.ogg']) :- S < 120, H is S div 60, pnumber(H, Ogg).
hours(S, [Ogg, 'hours.ogg']) :- H is S div 60, pnumber(H, Ogg).
time(Sec) -- ['less_a_minute.ogg'] :- Sec < 30.
time(Sec) -- [H, '1_minute.ogg'] :- tts, S is round(Sec/60.0), hours(S, H), St is S mod 60, St = 1, pnumber(St, Ogg).
time(Sec) -- [H, Ogg, 'minutes.ogg'] :- tts, S is round(Sec/60.0), hours(S, H), St is S mod 60, pnumber(St, Ogg).
time(Sec) -- [Ogg, 'minutes.ogg'] :- not(tts), Sec < 300, St is Sec/60, pnumber(St, Ogg).
time(Sec) -- [H, Ogg, 'minutes.ogg'] :- not(tts), S is round(Sec/300.0) * 5, hours(S, H), St is S mod 60, pnumber(St, Ogg).


%%% distance measure
distance(Dist, Y) -- D :- measure('km-m'), distance_km(Dist, Y) -- D.
distance(Dist, Y) -- D :- measure('mi-f'), distance_mi_f(Dist, Y) -- D.
distance(Dist, Y) -- D :- measure('mi-y'), distance_mi_y(Dist, Y) -- D.

%%% distance measure km/m
distance_km(Dist, nominativ) -- [ X, 'meters_nominativ.ogg']                  :- Dist < 100,   D is round(Dist/10.0)*10,           dist(D, X).
distance_km(Dist, dativ) --     [ X, 'meters_dativ.ogg']                      :- Dist < 100,   D is round(Dist/10.0)*10,           dist(D, X).
distance_km(Dist, nominativ) -- [ X, 'meters_nominativ.ogg']                  :- Dist < 1000,  D is round(2*Dist/100.0)*50,        dist(D, X).
distance_km(Dist, dativ) --     [ X, 'meters_dativ.ogg']                      :- Dist < 1000,  D is round(2*Dist/100.0)*50,        dist(D, X).
distance_km(Dist, nominativ) -- ['around_1_kilometer_nominativ.ogg']          :- Dist < 1500.
distance_km(Dist, dativ) --     ['around_1_kilometer_dativ.ogg']              :- Dist < 1500.
distance_km(Dist, nominativ) -- ['around.ogg', X, 'kilometers_nominativ.ogg'] :- Dist < 10000, D is round(Dist/1000.0),            dist(D, X).
distance_km(Dist, dativ) --     ['around.ogg', X, 'kilometers_dativ.ogg']     :- Dist < 10000, D is round(Dist/1000.0),            dist(D, X).
distance_km(Dist, nominativ) -- [ X, 'kilometers_nominativ.ogg']              :-               D is round(Dist/1000.0),            dist(D, X).
distance_km(Dist, dativ) --     [ X, 'kilometers_dativ.ogg']                  :-               D is round(Dist/1000.0),            dist(D, X).

%%% distance measure mi/f
distance_mi_f(Dist, nominativ) -- [ X, 'feet_nominativ.ogg']                  :- Dist < 160,   D is round(2*Dist/100.0/0.3048)*50, dist(D, X).
distance_mi_f(Dist, dativ) --     [ X, 'feet_dativ.ogg']                      :- Dist < 160,   D is round(2*Dist/100.0/0.3048)*50, dist(D, X).
distance_mi_f(Dist, nominativ) -- ['1_tenth_of_a_mile_nominativ.ogg']         :- Dist < 241.
distance_mi_f(Dist, dativ) --     ['1_tenth_of_a_mile_dativ.ogg']             :- Dist < 241.
distance_mi_f(Dist, nominativ) -- [ X, 'tenths_of_a_mile_nominativ.ogg']      :- Dist < 1529,  D is round(Dist/161.0),             dist(D, X).
distance_mi_f(Dist, dativ) --     [ X, 'tenths_of_a_mile_dativ.ogg']          :- Dist < 1529,  D is round(Dist/161.0),             dist(D, X).
distance_mi_f(Dist, nominativ) -- ['around_1_mile_nominativ.ogg']             :- Dist < 2414.
distance_mi_f(Dist, dativ) --     ['around_1_mile_dativ.ogg']                 :- Dist < 2414.
distance_mi_f(Dist, nominativ) -- ['around.ogg', X, 'miles_nominativ.ogg']    :- Dist < 16093, D is round(Dist/1609.0),            dist(D, X).
distance_mi_f(Dist, dativ) --     ['around.ogg', X, 'miles_dativ.ogg']        :- Dist < 16093, D is round(Dist/1609.0),            dist(D, X).
distance_mi_f(Dist, nominativ) -- [ X, 'miles_nominativ.ogg']                 :-               D is round(Dist/1609.0),            dist(D, X).
distance_mi_f(Dist, dativ) --     [ X, 'miles_dativ.ogg']                     :-               D is round(Dist/1609.0),            dist(D, X).

%%% distance measure mi/y
distance_mi_y(Dist, nominativ) -- [ X, 'yards_nominativ.ogg']                 :- Dist < 241,   D is round(Dist/10.0/0.9144)*10,    dist(D, X).
distance_mi_y(Dist, dativ) --     [ X, 'yards_dativ.ogg']                     :- Dist < 241,   D is round(Dist/10.0/0.9144)*10,    dist(D, X).
distance_mi_y(Dist, nominativ) -- [ X, 'yards_nominativ.ogg']                 :- Dist < 1300,  D is round(2*Dist/100.0/0.9144)*50, dist(D, X).
distance_mi_y(Dist, dativ) --     [ X, 'yards_dativ.ogg']                     :- Dist < 1300,  D is round(2*Dist/100.0/0.9144)*50, dist(D, X).
distance_mi_y(Dist, nominativ) -- ['around_1_mile_nominativ.ogg']             :- Dist < 2414.
distance_mi_y(Dist, dativ) --     ['around_1_mile_dativ.ogg']                 :- Dist < 2414.
distance_mi_y(Dist, nominativ) -- ['around.ogg', X, 'miles_nominativ.ogg']    :- Dist < 16093, D is round(Dist/1609.0),            dist(D, X).
distance_mi_y(Dist, dativ) --     ['around.ogg', X, 'miles_dativ.ogg']        :- Dist < 16093, D is round(Dist/1609.0),            dist(D, X).
distance_mi_y(Dist, nominativ) -- [ X, 'miles_nominativ.ogg']                 :-               D is round(Dist/1609.0),            dist(D, X).
distance_mi_y(Dist, dativ) --     [ X, 'miles_dativ.ogg']                     :-               D is round(Dist/1609.0),            dist(D, X).


interval(St, St, End, _Step) :- St =< End.
interval(T, St, End, Step) :- interval(Init, St, End, Step), T is Init + Step, (T =< End -> true; !, fail).

interval(X, St, End) :- interval(X, St, End, 1).

string(Ogg, A) :- voice_generation, interval(X, 1, 19), atom_number(A, X), atom_concat(A, '.ogg', Ogg).
string(Ogg, A) :- voice_generation, interval(X, 20, 95, 5), atom_number(A, X), atom_concat(A, '.ogg', Ogg).
string(Ogg, A) :- voice_generation, interval(X, 100, 900, 50), atom_number(A, X), atom_concat(A, '.ogg', Ogg).
string(Ogg, A) :- voice_generation, interval(X, 1000, 9000, 1000), atom_number(A, X), atom_concat(A, '.ogg', Ogg).

dist(X, Y) :- tts, !, num_atom(X, Y).

dist(0, []) :- !.
dist(X, [Ogg]) :- X < 20, !, pnumber(X, Ogg).
dist(X, [Ogg]) :- X < 1000, 0 is X mod 50, !, num_atom(X, A), atom_concat(A, '.ogg', Ogg).
dist(D, ['20.ogg'|L]) :-  D < 30, Ts is D - 20, !, dist(Ts, L).
dist(D, ['30.ogg'|L]) :-  D < 40, Ts is D - 30, !, dist(Ts, L).
dist(D, ['40.ogg'|L]) :-  D < 50, Ts is D - 40, !, dist(Ts, L).
dist(D, ['50.ogg'|L]) :-  D < 60, Ts is D - 50, !, dist(Ts, L).
dist(D, ['60.ogg'|L]) :-  D < 70, Ts is D - 60, !, dist(Ts, L).
dist(D, ['70.ogg'|L]) :-  D < 80, Ts is D - 70, !, dist(Ts, L).
dist(D, ['80.ogg'|L]) :-  D < 90, Ts is D - 80, !, dist(Ts, L).
dist(D, ['90.ogg'|L]) :-  D < 100, Ts is D - 90, !, dist(Ts, L).
dist(D, ['100.ogg'|L]) :-  D < 200, Ts is D - 100, !, dist(Ts, L).
dist(D, ['200.ogg'|L]) :-  D < 300, Ts is D - 200, !, dist(Ts, L).
dist(D, ['300.ogg'|L]) :-  D < 400, Ts is D - 300, !, dist(Ts, L).
dist(D, ['400.ogg'|L]) :-  D < 500, Ts is D - 400, !, dist(Ts, L).
dist(D, ['500.ogg'|L]) :-  D < 600, Ts is D - 500, !, dist(Ts, L).
dist(D, ['600.ogg'|L]) :-  D < 700, Ts is D - 600, !, dist(Ts, L).
dist(D, ['700.ogg'|L]) :-  D < 800, Ts is D - 700, !, dist(Ts, L).
dist(D, ['800.ogg'|L]) :-  D < 900, Ts is D - 800, !, dist(Ts, L).
dist(D, ['900.ogg'|L]) :-  D < 1000, Ts is D - 900, !, dist(Ts, L).
dist(D, ['1000.ogg'|L]):- Ts is D - 1000, !, dist(Ts, L).