## 队列
除并发应用，Queue在Java SE5中仅有两个实现 LinkedList和PriorityQueue,差异在于排序行为，而不是性能。

除了优先级队列，Queue将准确地按照元素被置于Queue中的顺序产生它们。



## Map

映射表（也称为关联数组）的基本思想：它维护的是键-值（对）关联，因此可以用键来查找值。

标准的java库包含了Map的几种基本实现：HashMap,TreeMap,LinkedHashMap,WeakHashMap,ConcurrentHashMap,IdentityHashMap。

它们都有相同的基本接口Map，但是行为特性各不相同，这表现在效率、键值对的保存及呈现次序、对象的保存周期、映射表如何在多线程程序中工作和判定“键”等价的策略等方面。

### 性能
性能是映射表中的一个重要问题。当get()中使用线性搜索时，执行速度会相当慢，这正是HashMap提高速度的地方。

HashMap使用了特殊的值，称作散列码，来取代对键的缓慢搜索。

散列码是“相对唯一”的、用以代表对象的int值，它通过将该对象的某些信息进行转换而生成。

hashCode()是根类Objcet中的方法，因此所有Java对象都能 产生散列码，

HashMap就是使用对象的hashCode()进行快速查询的，此方法能够显著提高性能。

使用数组代替溢出捅，有两个好处：
- 可以针对磁盘存储方式做优化。
- 在创建和回收单独的记录时，能节约很多时间。

下面是基本Map实现的对照表，如果没有其他的限制，应该默认选择HashMap,因为它对速度做了优化，其他实现强调了其他的特性，因此都不如HashMap快。

| Map实现类型 | 具体特性 |
| --- |  --- |
| HashMap | Map基于散列表的实现（它取代了Hashtable）。插入和查询“键值对”的开销是固定的。可以通过构造器设置容量和负载因子，以调整容器的特性。|
| LinkedHashMap | 类似HashMap,但迭代遍历它时，取得“键值对”的顺序是其插入次序，或者是最近最少使用（LRU）的次序。|
| TreeMap | 基于红黑树的实现。查看“键”或者“键值对”时，它们会被排序（次序由Comparable或者Comparator决定）。TreeMap的特点在于：所得到的结果是经过排序的。TreeMap是唯一的带有subMap()方法的Map，它可以返回一个子树。|
| WeakHashMap | 弱键（weak key）映射，允许释放映射所指向的对象；这是为解决某类特殊问题而设计的。如果映射之外没有引用指向某个“键”，则此键可以被垃圾收集器回收。 |
| ConcurrentHashMap | 一种线程安全的Map, 它不涉及同步加锁。|
| IdentityHashMap | 使用== 代替equals()对“键”进行比较的散列映射。专为解决特殊问题而设计。 |

散列是映射中存储元素时最常用的方式。

对Map中使用的键的要求与对Set中的元素要求一样：

- 任何键必须具有一个equals()方法。
- 如果键被用于散列Map,那么它必须还具有恰当的hashCode()方法。
- 如果键被用于TreeMap,那么它必须实现Comparable。

### SortedMap
TreeMap 是其现阶段的唯一实现。

## 散列与散列码

Object的hashCode()方法生成散列码，默认是使用对象的地址计算散列码。

默认的Objcet.equals()只是比较对象的地址。

若要使用自己的类作为HashMap的键，必须同时重载hashCode()和equals()。

如果不为你的键覆盖hashCode()和equals()，那么散列的数据结构（HashSet, HashMap, LinkedHashSet, LinkedHashMap）就无法正确处理你的键。

使用散列的目的在于：想要使用一个对象来查找另一个对象。

### 正确的equals()方法必须满足的5个条件
- 1.自反性。对任意x，x.equals(x)一定返回true.
- 2.对称性。对任意x和y，如果y.equals(x)返回true，则x.equals(y)也返回true.
- 3.传递性。对任意x、y、z，如果有x.equlas(y)返回true，y.equals(z)返回true，则x.equals(x)一定返回true。
- 4.一致性。对任意x和y，如果对象中用于等价比较的信息没有改变，那么无论调用多少次x.equals(y)，返回的结果应该保持一致，一直是true或false。
- 5.对任何不是null的x，x.equals(null)一定返回null。

### 散列的价值在于速度
散列使得查询得意快速进行。它将键保存在某处，以便能够快速找到。存储一组元素最快的数据结构是数组，所以用它来保存键的信息（而不是键本身）。

因为数组不能调整容量，而我们希望在Map中保存数量不确定的值，如何保证键的数量不被数组的容量限制？

答案是：**数组并不保存键本身。而是通过键对象生成一个数字，将其作为数组的下标**，这个数字就是散列码，由定义在Objcet中的、且可能由你覆盖的hashCode()方法（在计算机科学的术语中成为散列函数）生成。

不同的键可以产生相同的下标，可能会冲突，但数组多大就不重要了，任何键都能找到自己的位置。

查询一个值的过程首先是计算散列码，然后使用散列码查询数组。如果能保证没有冲突（当值的数量是固定的，那就有可能），就有了一个完美的散列函数，但仅是特例。

完美的散列函数在SE5中的EnumMap和EnumSet中得到了实现，因为enum定义了固定数量的实例。

**通常冲突由外部链接处理**：数组并不直接保存值，而是保存值的list。然后对list中的值使用equals()方法进行线性查询，这部分查询自然比较慢，但如果散列函数好的话，数组的每个位置只有少量的值。因此不是查询整个list,而是快速的调到数组的某个位置，只对很少的元素进行比较，这就是HsahMap如此快的原因。

由于散列表中的“槽位”（slot）通常称为桶位（bucket），因此我们将表示实际散列表的数组命名为bucket。为使散列分布均匀，桶的数量通常使用质数。

## 选择接口的不同实现
Hashtable、Vector和Stack:过去遗留下来的类，目的只是为了支持老的程序，新程序最好不要使用。

### List
ArrayList底层由数组支持，LinkedList由双向链表实现，其中每个对象包含数据的同时还包含指向链表中前一个与后一个元素的引用。

如果经常在表中插入或删除元素，LinkedList比较合适（LinkedList还有建立在AbstractSequencetialList基础上的其他功能），否则应该使用速度更快的ArrayList。

CopyOnWriteArrayList是List的一个特殊实现，专门用于并发编程。

### Set
- HashSet最常用，查询速度最快；
- LinkedHashSet保持元素插入的次序；
- TreeSet基于TreeMap,生成一个总是处于排序状态的Set.