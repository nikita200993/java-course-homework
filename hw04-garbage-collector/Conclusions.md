# Сравнение раздичных сборщиков мусора

## Аргументы jvm
-XX:+<collector name>
-Xms<heap size>
-Xmx<heap size>
-Xlog:gc:file=./logs/gc_pid_%p.log
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=./logs/memdump

Где `collector name` принимал значения - "UseParallelGC" и 
"G1GC", а `heap size` - "256m", "10g". Приложение работало до падения от `OOM`.

##256мб heap

 GC name  | Young count | Full GC count | Avg pause time, ms | Max pause, ms | Total pause time | Throughput, % | Application run duration |
|----------|-------------|---------------|--------------------|---------------|------------------|---------------|--------------------------|
| Parallel | 435         | 204           | 268                | 711           | 2min 51sec       | 55            | 6min 24sec               |
| G1       | 988         | 6             | 23                 | 182           | 28sec            | 89            | 4min                     |

Не верю в throughput, parallel, насколько я знаю, должен давать лучший throughput. \
Бенчмарк очень примитивный, возможно в этом причина. Остальные показатели, \
вроде правдоподобны. `G1GC` гораздо чаще запускает сборки, чтобы улучшить \
отзывчивость приложения. Среднее и масимальное время пауз гораздо меньше. \
У `G1GC` были еще другие фазы сборки, которые выполнялись \
паралелльно с приложением, но их соотнести с `ParallelGC` не получается, \
поэтому в таблице они не присутствуют.

##heap 10гб

| GC name  | Young count | Full GC count | Avg pause time | Max pause  | Total pause time | Throughput, % | Application run duration |
|----------|-------------|---------------|----------------|------------|------------------|---------------|--------------------------|
| Parallel | 34          | 0             | 1sec 170ms     | 2sec 170ms | 40sec            | 93            | 10min 3sec               |
| G1       | 47          | 0             | 143ms          | 370ms      | 7sec             | 97            | 4min 20 sec              |

При одинаковом `throughput` такое разное время работы программы. Такое ощущение \
что это два разных приложения, время работы никак нельзя объяснить паузами. Возможно \
причина в том случае `ParallelGC` приложение насоздавало 50гб объектов, в случае
`G1GC` - 17гб, но почему так происходит из за смены сборщика, я не понял. Также,  \
не ясно почему на большем хипе не было ни одной полной сборки. Качественные выводы по сравнению двух сборщиков
такие же как и для 256мб.

## Выводы

### Про размер хипа

Больше хип, реже сборки, но дольше.

### Про сборщики

Если верить этому бенчмарку, то G1GC лучше прямо во всем. Но учитывая странные обстоятельства,
я бы таких выводов не делал=).                                                                                                     