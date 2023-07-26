# Skip List Set

<img width ="500px" src="https://camo.githubusercontent.com/d49b9f48546f2d9d2531e9cdee0f9a8298ab9c5fd7dff9d2be1699593fcbde85/68747470733a2f2f692e7974696d672e636f6d2f76692f6e306b3530304e6b364d452f6d617872657364656661756c742e6a7067"/>

### To Do
```
CASE: 100,000 integers, 1,000 finds, 500 removals.  Generating...
  LinkedList  add:      8ms  find:    143ms  del:     71ms  (501 missing) find:    226ms  
  SkipListSet add:    149ms  (518 missing) find:  5,549ms  del:      6ms  (518 missing) find:  5,630ms  
                                             bal:    144ms  (518 missing) find:  2,158ms  
  TreeSet     add:     73ms  find:      1ms  del:      1ms  (501 missing) find:      3ms  

CASE: 1,000,000 integers, 10,000 finds, 5,000 removals.  Generating...
  SkipListSet add:    166ms  (9,331 missing) find: 33,652ms  del:     18ms  (9,331 missing) find: 33,892ms  
                                             bal:     98ms  (9,331 missing) find: 33,241ms  
  TreeSet     add:  1,036ms  find:     16ms  del:      9ms  (5,018 missing) find:     22ms  

CASE: 10,000,000 integers, 10,000 finds, 5,000 removals.  Generating...
  SkipListSet add:    139ms  (9,974 missing) find: 42,863ms  del:     10ms  (9,974 missing) find: 40,836ms  
                                             bal:     76ms  (9,974 missing) find: 17,036ms  
  TreeSet     add: 16,424ms  find:     19ms  del:     10ms  (5,001 missing) find:     19ms 
```
- [x] Set minimum height based on how many items are in the list to prevent thrashing
- [x] Reimplement rebalance so that it doesn't set a new height
- [x] Issue when inserting new head
- [x] Add All
- [x] Clear
- [x] Comparator
- [x] Contains
- [x] Contains All
- [x] Equals
- [x] Hash Code
- [x] IsEmpty
- [x] Remove All
- [x] Retain All
- [x] Size
- [x] SubSet
- [x] First
- [x] Last
- [x] HeadSet
- [x] Tail Set
- [x] To Array
- [x] ReBalance
