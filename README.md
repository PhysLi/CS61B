# CS61B
## Lecture 1: Introduction
- Java初步
    - 在Java中函数必须作为类的方法存在，用`public static`来声明一个函数；如果要运行一个class，则该class的方法中必须包含一个`main()`
    - 所有变量（包括函数）都需有一个声明的类型，称为静态类型语言(static typed，即变量需要声明类型，且只能存储该类型的数据。使语言更快一些)。
    - `.java`文件先经过javac编译（需要检查所有类型是否与声明匹配）为`.class`文件，再通过java解释器运行。
    - 分号用来指示语句的终点和下一个语句的起点；大括号用来将多个语句打包起来
    - 整型数组是一个class，类名为`int[]`，于是声明一个数组需要调用构造函数，构造的数组是一个对象，其具有一个attribute `length`存储数组长度：
      ```java
      int[] numbers = new int[]{4, 7, 10};
      int[] nums = new int[3];
      int[] z = {1,2,3,4};
      nums[0] = 1;
      nums[1] = 2;
      nums[2] = 3;
      System.out.println(number.length);
      ```
      可以用`System.arraycopy(<source>,<start>,<target>,<target-start>,<length>)`
    - `for`循环：分为initialization,termination,increment三部分，其中每一部分都可以有多个由`,`分割的子表达式：
      ```java
      for (<initialization>; <termination>; <increment>){
        <clause>
      }
      for (int i = 0, j = 10; i < j; i += 1){
        System.out.println(i + j);
      }
      ```
      `for`循环也可以不指定循环变量，而直接在一个iterable中循环：
      ```java
      String[] a = {"cat", "dog", "laser horse", "ketchup", "horse", "horbse"};

      for (String s : a) {
          for (int j = 0; j < 3; j += 1) {
              System.out.println(s);
              if (s.contains("horse")) {
                  break;
              }                
          }
      }
      ```
## Lab01: Set up
### Git
- reference: https://sp24.datastructur.es/resources/guides/git/
- Local repository
  - Git是分布式版本管理系统，分布式意味着每个合作者的设备中都保存着项目的完整历史记录（项目的完整历史记录即称为仓库,repository）。
  - 将directory初始化为repository：命令行在该目录下运行`git init`
  - 指定directory中哪些文件是需要被版本管理的：`git add <filename>`，这个文件称为staged；每次对文件进行修改后，这些changes也需要被staged才能被进一步提交(committed)，所以修改之后需要重新进行 `git add`；也可以直接stage一整个文件夹。
  - 将所有被`add`的文件的当前版本提交到repository：`git commit -m "comment"`，会在repository中存储被track的文件的一个snapshot（`-m`的作用是可以添加一个comment）
  - 对最近的commit做补充：`git commit --amend`，可以是staged了一些忘记的更改，也可以是更改了comment。会覆盖最后一次commit。
  - 可以用`git status`查看当前repository状态，`git log`查看提交日志，提交日志里对每个提交都有一个ID，可以用`git show <id>`来具体查看某次提交的详细情况。
  - 版本回退：
    - 可以通过 `git checkout <commit-id>`来查看某次commit后的repo状态，再通过`git checkout main`来返回。若`git status`不clean（即有没有commit的更改），则无法`checkout`；可以通过`git checkout <commit-id> --<filename>`来恢复特定的某个文件。
    - `git restore --staged <filename>`，可以将某个文件从staged变为modified（不再想提交某个更改）；
    - 若一个文件目前是unstaged状态，可以通过`git restore --source = <commit id or branch> <filename>`回退到某次commit的版本（但不影响commit历史），若省略`--source`则回退到最近一次commit的状态；
  - repository中文件的状态分为以下几种：

    ![](./note_img/file-status.png)
- Branching
  - 通过branching可以同时track项目的不同版本。默认的branch称为`main`。整个repo是树状结构，以commit作为节点
  - `git branch <new-branch-name>`：从当前branch创建新branch
  - `git switch <wanted-branch>`：将working directory切换到另一个branch；`git switch -c <new-branch-name>`能够创建同时切换。
  - `git branch -d <branch>`可以删除分支
  - `git branch -v`可以查看当前所处的是哪个分支
  - 在某个branch中执行`git merch <branch-to-merge>`可以将另一个分支和这个分支结合（通过创建一个新的commit）
  - 每一个branch有一个指针，指向该branch的最新commit；每个repo有一个`HEAD`指针，指向当前所在的commit，其中切换branch、`git checkout`、建立新commit都会使`HEAD`指针移位
- Remote repository
  - 把local repo和remote repo建立联系
    - `git clone <remote-repo-URL>`：在本地创建远程repo即其所有commit的一个copy，并以其最近的commit为范本创建working directory。此时最近的commit上不仅有`main`指针，还有一个`origin/main`指针（即代表本地repo的origin版本在远程repo上，这个指针用于本地和远程的同步）
  - 向remote repo提交数据
    - `git push <remote-repo-name> <branch>`：将本地文件的最近copy上传到某个branch：当在本地进行了几个commit后，本地的`main`指针指向了最新的commit，但`origin/main`指针仍指向上次push的commit（称为`n commits ahead of 'origin/main'`）。使用`git push`后，`origin/main`指针移动到本地最新的commit，使得远程的`main`指针也发生移动，从而实现本地和远程的同步。
  - 从remote repo获取数据：对于合作项目，多个合作者的本地repo都和远程repo相连，各自修改并提交commit和push。所以常见的情况是对于某些文件，本地repo的commit领先于远程repo；而对于另一些文件，本地repo的commit落后于远程repo，此时你不能push，因为push之前本地repo和远程repo需要是同步的，所以需要先`git pull`来进行同步再push。
    - `git fetch <remote-repo-name>`：从本地repo和远程repo分叉的那个commit开始将远程repo的commit下载到本地作为一个新branch
    - `git pull <remote-repo-name> main`：在`fetch`的基础上将下载的branch来merge到本地branch中，并提交一个新的commit，此时本地的`main`指向merge后的新commit，此时本地的修改可以看做是从下载的branch到新commit的修改，从而可以push。
    - 若远程的未同步的commit和本地的未push的commit针对同一地方进行了冲突的修改，进行merge的时候会发生merge conflict，需要解决冲突后手动再次commit。
  - 本地repo和多个远程repo写作
    - `git remote add <remote-repo-name> <remote-repo-URL>`：记录一个新的数据传输位置
    - `git remote -v`：列出所有数据传输的位置
    - 多个远程仓库的push和pull关键是知道远程repo的`main`指针和本地的`main`指针分别指向哪个commit
    

  
## Lecture 2: Define and Using Classe
### Attribute
- java可以在class中声明attribute，需要在类型之前添加identifier：`public`；
- 若某个class具备一个构造函数(constructor)，则可以将其实例化（即定义一个属于该class的instance）。构造函数没有返回值，需与类名同名：实例化时需要使用`new`关键字再调用构造函数。
  ```java
  public class Dog{
    public int N;
    public Dog(int n){
      N = n;
    }
  }
  Dog a = new Dog(2);
- 我们不定义instance也可以直接调用class的method：若在相同的class中，则无需显式地写出class name；若想再其它class的method中调用，则需要用`<class-name>.<method-name>()`；若该method使用了该类中未赋值的attribute，则必须作为instance method而非class method被调用，称为non-static method，在函数声明处不能加`static`。

### Static vs. non-static
- static method是class method，可以只用类名来调用；non-static method是instance method（需要instance attribute）；
- static method的目的是：有些class在程序中不会被实例化，如`math` class。
- 事实上attribute也分为static和non-static，等价于python中的class attribute和instance attribute。
- static attribute也可以被instance来access。

### Intellij Debugger
- 加断点：暂停后可以逐行运行，可以显示变量状态；也可以在暂停时在box中evaluate一个表达式。
- 代码错误一般可以追溯到一些函数的错误。debug的科学方法是：首先做出一些关于函数应该如何返回的假设，然后设置断点并`step over`这个函数来验证你的假设。若假设正确，则继续向下运行（做出下一个假设）；若假设没有满足，则应`step into`来进入函数体进一步诊断。
- Java Visualizer可以显示“框-指针”图
- resume即恢复运行，意为运行直到下一个断点。
- 右键单击断点可以设置条件断点。

## Lecture 3: Lists | References, recursion and lists
### Primitive types
- 数据以bit string的形式存储在memory中，不同的Java type可以将同一个bit string翻译为不同的数据
- Java有8中primitive type：byte, short, int, long, float, double, boolean, char
- 在声明某种类型的变量时，进行内存分配，即为声明的这个变量按照类型分配了一定大小的内存；当为变量赋值时将值存储到这个内存空间中
- 等号黄金法则：已有变量`x`,`y`，则`y = x`只是将`x`内存中存储的数据赋值到`y`的内存中

### Reference types
- 除了primitive type，其它所有类型（包括数组）都称为Reference type
- 将Reference type对象实例化时（`new`一个instance），实际上是创建了instance的一个引用（`new`关键字返回的是instance的地址），于是Reference class对象变量对应的内存地址中实际上存储的是instance的地址（这也是为什么称为Reference class，因为实际上存储的是Reference）。
- equal and copy
  ```java
  int[] a = new int[]{1,2,3,4}, b, c;
  a = b;
  c = new int[4];
  System.arraycopy(a,0,c,0,4);
  ```
### 参数传递(Parameter passing)
- 函数的形参也是一种声明，传参过程和使用`=`过程是相同的。

### IntList and linked data structure
- Java中数组的大小是固定的，但我们想要一种可变长度的列表，可以通过链表的方式实现
```java
public class IntList{
  public int first;
  public IntList rest;

  public IntList(int f, IntList r) {
    first = f;
    rest = r;
  }

  public static void main(String[] args) {
    IntList L = new IntList(1, new IntList(2, null));
  }
}

```

### Lecture 4: SLLists, Nested Classes, Sentinel Nodes
- 之前定义的`IntList`属于裸递归数据结构（其构造函数需要制定first和rest，explicitly在递归地进行构造）。现定义一个新的`SLList`类来解决这个问题(adding clothing)，起到中介作用，无需自行处理递归链接（即原有的`IntList`本质上是节点，现在的`SLList`是节点的集合）
- Access control：`IntNode`是`SLList`的私有attribute，使得其它类中的代码不能access这个attribute。使用private attribute可以隐藏class的实现细节（即public method决定了用户如何使用，不能随意修改；而private method只隐藏在class的构造内部，所以可以修改）
- Nested class：用户不会使用`IntNode`类，这个类只会被`SLList`类使用，所以`IntNode`类的声明可以放在`SLList`类内部，形成嵌套的类。
```java
public class SLList {

  private class IntNode {
    public int item;
    public IntNode next;

    public IntNode(int i, IntNode n) {
      item = i;
      next = n
    }
  }

  private IntNode first;

  public SLList(int x) {
    first = new IntNode(x, null);
  }

  public SLList() {
    first = null;
  }

  public void addFirst(int x) {
    first = new IntNode(x, first);
  }

  public int getFirst() {
    return first.item
  }

  public void addLast(int x) {
    if (first == null) {
      first = new IntNode(x, null);
    } else {
      while (p.next != null) {
        p = p.next;
      }
      p.next = new IntNode(x, null);
    }
  }

  private int size(IntNode p) {
    if (p.next == null) {
      return 1;
    } else {
      return 1 + size(p.next);
    }
  }

  public int size() {
    return size(first);
  }
}
```

- `size()`方法是 $O(N)$的。想要加速的话可以将size设置为`SLList`的一个attribute从而持续追踪该项(缓存法，cache，空间换时间)。
- `addLast()`方法有一段代码来专门解决空列表的特殊情况，而为了特殊情况而专门写的代码会使代码更难调试。解决方法是建立一个sentinel节点，它永远在列表的第一位，但没有意义，只是为了保证`first`不是`null`。

## Lecture 4: DLLists and arrays

### 双链表
- 如果列表变得很大，则`addLast()`,`getLast()`,`removeLast()`都需要从头开始找到最后一位，很慢；如果添加一个attribute指向最后一个节点，则前两个method都会变快，但`removeLast()`需要将这个指针更新使其指向原来的倒数第二个节点，仍需从头开始找。若为双链表结构则不存在这个问题（`SLList`指singly-linked list，`DLList`指doubly-linked list）
- 双链表包含两个子链表：前侧的双链表和后侧的双链表。
- 双链表需要有前后两个哨兵节点；或者可以建立循环哨兵(circular sentinel)。
- Generic Lists：希望list不只能够容纳`int`，也可以容纳其它数据类型，需要使用泛型(generics)`<placeholder>`
  ```java
  public class DLList<generics> {
    public GenericNode SentinelCirc;

    private class GenericNode {
      public generics item;
      public GenericNode next;
      public GenericNode prev;

      public GenericNode(generic x, GenericNode n, GenericNode p) {
        item = x;
        next = n;
        prev = p;
      }
    }

    public DLList() {
      sentinel = new GenericNode(null, sentinel, sentinel);
    }

    public static void main(String[] args) {
      DLList<String> L1 = new DLList<>("a");
      DLList<Integer> L1 = new DLList<Integer>(1);
    }
  }
  ```

### 二维数组
二维数组的每个行存储的是一个一维数组的Reference
```java
int[][] a;
a = new int[4][] //4行x列；
a[0] = new int[1];
a[1] = new int[2];//不同行的列数可以不同
```


## Lecture 5: Testing
- 当拿到一个问题，与其直接开始写代码，也可以从写测试用例开始来编写程序，称为测试驱动的开发(test-driven development, TDD)。
  - 确定需要增加一个新的特性；
  - 为这个特性写一个单元测试(unit test)；
    - “单元”的意思是需要代码拆分成最小的可测试片段，这就要求每一个函数只做一件事。
  - 编写代码直到测试通过。
- 测试的基本方法就是给定输入，我们得到一个输出，然后检查该输出和与其的输出是否相同，若不同则给出报错信息。这个过程非常普遍，以至于可以直接调用库来进行。可以使用`Truth`库（由Google编写）
- 测试的例子作为一个测试类的method存在：
  - 所以需要先`import Truth`
  - 创建一个测试类
  - 再创建一些对应测试例子的无参数method（需要在每个method的前一行加上`@Test`，使该method变成non-static）
  - method中需要声明`input`,`expected`,`actual`
  - 进行断言`assertThat(actual).isEqualTo(expected)`
- 测试的例子一般会涵盖一些特殊情况（称为edge case）


## Lecture 7: Arrays and Lists
### arrayList
- 双链表只解决了末尾添加、查找、删除的问题，但大规模链表的random access仍很慢。与之相反，数组由于每个box在内存中有固定的大小和位置，其Random access具有常数的时间复杂度。这启发我们用ArrayList解决Random access问题。
- 想要用有限尺寸的数组构建无限尺寸的列表该怎么做？
  - 仍需`getLast()`,`addLast()`,`removeLast()`,`get(int i)`等public method
  ```java
  public class AList {
    private int[] items;
    private int size;

    public AList() {
      items = new int[100];
      size = 0;
    }

    public static void addLast(int x) {
      size++;
      item[size] = x;
    }

    public static int getLast(int x) {
      return item[size - 1];
    }

    public static int get(int i) {
      return item[i];
    }

    public static int removeLast() {
      int x = getLast();
      size--;
      return x;
    }
  }
  ```

### Resizing the arrayList
- 当`item`数组填满时，建立一个新的大数组，把小数组的内容copy进去，再使`item`指向新数组。
  ```java
  private void resize(int capacity) {
    int[] a = new int[capacity];
    System.arraycopy(items, 0, a, 0, size);
    items = a;
  }

  public static void addLast(int x) {
    if (size == item.length) {
      resize(size + 1);
    }
    size++;
    item[size] = x;
  }
  ```
  这样的实现会使在容量超过后每次`addLast()`都会变得更慢（因为每次`resize()`都需要重新创建一个大数组。解决方案是
  ```java
    if (size == item.length) {
      resize(2 * size);
    }
  ```
  即用指数的空间来换时间。
- 这样的实现可能导致，当删除了很多数据时，很多空间浪费了。所以还需要有一个方法在删除数据后缩小backing array。

### 泛型化
- Java不允许实例化(`new`)泛型类型的数组，即`new GenericType[100]`是不允许的。
- 需要使用`(GenericType[]) new Object[100]`（即需要进行强制类型转换）
- 当数组为泛型数组时，进行`removeLast()`时需要`item[size - 1] = null`来使java回收存储泛型的内存来节省空间（`int`类型不太占空间所以不需要）。


## Lecture 8: Inheritance 1, interface and implementation inheritance

### Interface inheritance
- 前述创建的 `SLList`,`DLList`,`AList`基本上有相似的method。若在实际使用中一个函数接受的参数本来是`SLList`，想改成`AList`，则需要将函数重新实现一遍（称为重载，overloading，两个函数有相同的signature但接受不同的参数）。但若有多种情况，需要重载很多次，很麻烦，不容易维护。
- 上位词(hypernym)：一些例子所属的一般的类型称为这些例子的上位词，这些例子称为下位词(hyponym)
  - `List`是`SLList`和`AList`的上位词，因为`SLList`和`AList`有相同的用户接口(interface)，所以可以首先用`interface`关键字定义一个基类`List`，需要声明所有`List`需要有哪些方法（指定what to do），但不给出具体实现（不指定how to do it，具体实现在子类中给出）。其中`@Override` tag只起注释作用（也可以用于检查是不是真的override了）。
  ```java
  public interface List61B<Item> {
    public void addFirst(Item x);
    public void addLast(Item x);
    public void insert(Item x, int position);
    public void addFirst(Item x);
    public void addLast(Item x);
    public Item getFirst();
    public Item getLast();
    public Item get(int i);
    public int size();
    public Item removeLast();
  }
  public class AList<Item> implements List61B<Item> {
    @Override
    public void addLast(Item x) {

    }
  }
  ```
  从而在使用这些类时，可以直接指定参数类型为`List61B`而不指定是哪种具体类型（不会有类型不匹配的问题）。
- 覆盖(overriding)和重载(overloading)：子类和基类有相同的signature，称为子类override了基类的method
- 上述过程称为Interface inheritance，其中Interface定义为上位词所有必须具备的public method的signature的列表。子类必须override所有method，不能有遗漏，否则会编译失败（Intellij可以自动给出需要override的method）。

### Implementation inheritance
- 子类可以既继承signature也继承implementation（和python相同），用`default`关键字来实现；在基类的implementation中可以直接使用未实现的Interface。若在子类中override，则会优先使用该子类的implementation而非default implementation。但在子类的method中可以使用`super`关键字（和`this`关键字相对）来调用被覆盖了的parent method。
  ```java
  public interface List61B<Item> {
    default public void print() {
      for (int i = 0; i < size(); i++) {
        System.out.print(get(i) + " ");
      }
      System.out.println("");
    }
  }
  ```

### Static type vs. Dynamic type
- 若声明`List61B<Integre> A = new SLList<>()`，则调用`A.print()`时会调用parent method还是子类method？
- 每个变量都具有两种类型：
  1. compile-time type或者称为static type，是变量在声明时指定的类型，即`A`的static type是`List61B`，永远不会改变；
  2. run-time type或者称为dynamic type，在实例化时指定（如使用`new`）的时候，是reference指向的对象的类型。
- 优先使用run-time type的方法（称为dynamic method selection）。

## Lecture 9: Inheritance 2: Extends, Casting higher order functions
### 另一种implementation inheritance：extends
- 若希望一个类成为另一个类的子类（而不是一个Interface的子类），需要使用`extends`关键字（而非`implements`关键字）
- 子类无法使用父类的private attribute，而需要使用`super`关键字来通过父类的public method进行访问
  ```java
  public class VengefulSLList<Item> extends SLList<Item> {
    SLList<Item> lostItems;

    public VengefulSLList() {
      super();
      lostItem = new SLList<>();
    }

    public VengefulSLList(Item x) {
      super(x);
      lostItem = new SLList<>();
    }

    @Override
    public Item removeLast() {
      Item x = super.removeLast();
      lostItems.addLast(x);
      return x;
    }
  }
  ```
- 在调用子类的构造函数时也会调用父类的构造函数（因为子类继承了父类的attribute，也需要进行构造）：`super()`，但若没有这一行也会自动调用。若构造函数有重载，则不会根据子类的构造函数传参情况来选择调用哪一个父类的构造函数，此时需要显式调用`super(<args>)`。


### Reflect on implementation inheritance
- Java中每一个类都是`Object`类的子类
- 继承需要在`is-a`关系中使用而非`has-a`关系。
- 栈(stack)只有`push()`,`pop()`两种public method

### 封装(Encapsulation)
- 是复杂性控制的一种方式。
- 若一个对象（或module模块）的实现细节完全隐藏，只能通过一系列public method（称为Interface）来交互，称这个对象是被封装的。
- 其它语言也可以封装，但java的`private`关键字可以让封装更加严密。
- implementation inheritance会破坏基类的封装：
  ```java
  public class Dog {
    public void bark() {
      System.out.println("bark");
    }
    public void barkMany(int N) {
      for (int i = 0; i < N; i++) {
        bark();
      }
    }
  }

  public class Dog {
    public void bark() {
      barkMany(1);
    }
    public void barkMany(int N) {
      for (int i = 0; i < N; i++) {
        System.out.println("bark");
      }
    }
  }
  ```
  两种实现应该不影响用户使用（封装）；但若进行implementation inheritance
  ```java
  public class VerboseDog extends Dog {
    @Override
    public void barkMany(int N) {
      System.out.println("Dog");
      for (int i = 0; i < N; i++) {
        bark();
      }
    }
  }
  ```
  则若`Dog`的实现是第2种会产生循环调用，进入死循环。


### 类型检查和Casting
- 编译器严格检查static type，会导致即使run-time type符合代码要求，但static type不符合，也会编译报错，如
  ```java
  public static Dog maxDog(Dog a, Dog b);
  public class Poodle extends Dog;
  Poodle a, b;
  Poodle c = maxDog(a, b);
  ```
  会报错。此时需要使用类型转换(cast)来改变compile-time type：`Poodle c = (Poodle) maxDog(a, b)`。但run-time type不会改变，所以如果进行了错误的casting通过了编译，则会在run-time报错。

### 高阶函数
- 不像python中一样，若想将函数作为参数传入函数中，但没有一个类型是“函数类型”，需要将函数包装到一个类中，然后将函数作为对象传入。
  ```java
  public interface IntUnaryFunction {
    public int apply(int x);
  }
  public class TenX implements IntUnaryFunction {
    @Override
    public int apply(int x) {
      return 10 * x;
    }
  }

  public static int doTwice(IntUnaryFunction f, int x) {
    return f.apply(f.apply(x));
  }
  ```

## Lecture 10: Inheritance 3: Subtype polymorphism, comparators, comparable

### 子类型多态性(subtype polymorphism)
- 多态：指为不同类型的实体提供一个统一接口（即统一Interface的不同implementation）
  ```java
  public interface Comparable<T> {
    public int comparaTo(T o);
  }
  public class Dog implements Comparable<Dog> {
    public int size;
    @Override
    public int comparaTo(Dog o) {
      return size - O.size;
    }
  }
  public static OurComparable max(OurComparable[] item) {
    int maxDex = 0;
    for (int i = 0; i < items.length; i++) {
      if (items[i].compareTo(item[maxDex]) > 0) {
        maxDex = i;
      }
    }
    return item[maxDex]
  }
  Dog[] dogs;
  Dog maxDog = (Dog) max(dogs);
  ```
  `Comparable`是java内置的接口。

### Comparators
- 若想要用不同的指标来进行比较，需要使用类似高阶函数的方法，传入不同的compare函数（需要封装到一个类`Comparator`中再传入）
  ```java
  public interface Comparator<T> {
    public int compare(T o1, T o2);
  }
  public class Dog implements Comparable<Dog>{
    public static class NameComparator implements Comparator<Dog> {
      @Override
      public int compare(Dog o1, Dog o2) {
        return o1.name.comparaTo(o2.name);
      }
    }
    public int comparaTo(Dog o);
  }

  ```
  > 在java中若想用高阶函数，则需要将函数变成一个类（如`NameComparator`；若想用子类型多态性来实现不同的高阶函数，则需要定义一个普适函数的Interface类（如`Comparator`）

## Lecture 11: Inheritance 4: Iterators, Object Methods
### Iterator
- 想要用数组来实现一个“集合”类
  ```java
  public class ArraySet<T> {
    private T[] items;
    private int size;

    public ArraySet() {
      items = (T[]) new Object[100];
      size = 0;
    }

    public void add(T x);
    public boolean contains(T x);
    public int size();
  }
  ```
  - 目前的`ArraySet`类不支持enhanced `for` loop，即不能够在`for`循环中作为iterable来使用`for (item x : Set)`。Enhanced `for` loop等价于
    ```java
    Iterator<Integer> seer = set.iterator();
    while (seer.hasNext()) {
      System.out.println(seer.next());
    }
    ```
    其中依赖于一个java标准类
    ```java
    public interface Iterator<T> {
      public boolean hasNext();
      T next();
    }
    所以需要在`ArraySet`类中添加该Interface的一个implementation，且必须让`ArraySet`继承`Iterable` interface才能将其直接用于enhanced `for` loop
    ```java
    public class ArraySet<T> implements Iterable<T> {
      private class ArraySetIterator implements Iterator<T> {
        private int Pos
        public ArraySetIterator() {
          Pos = 0;
        }
        @Override
        public boolean hasNext() {
          return Pos < size;
        }

        @Override
        public ArraySetIterator<T> next() {
          T returnItem = item[Pos];
          Pos++;
          return returnItem;
        }
      }

      public Iterator<T> iterator() {
        return new ArraySetIterator();
      }
    }

### Object Method
- `Object`类中有一个`String toString()` method，就是`System.out.println()`打印的字符串。在每个用户定义的类中可以自己进行override
- 另一个`Object` method是`boolean equals()`，和`==`的区别是：
  - `==`是逐比特比较，对于两个reference type的对象，比较两者的内存是否相同
  - `equals()`方法允许用户自己定义如何判断两者相等。
  ```java
  public class ArraySet<T> implements Iterable<T> {
    @Override
    public String toString() {
      StrinBuilder x = new StringBuilder();
      x.append("(");
      for (T i : this) {
        x.append(i.toString());
        x.append(" ");
      }
      x.append(")");
      return x.toString();
    }

    @Override
    public boolean equals(Object o) {
      if (o instanceof ArraySet x) {
        if (size != x.size) {
          return false;
        } else {
          for (T i : x) {
            if (!contains(i)) {
              return false;
            }
          }
          return true;
        }
      }
      return false;
    }
  }
  ```
  其中的`instanceof`关键字的用法是`<instance> instance of <class> <new-instance>`，判断某instance是否是某个类，若是的话进行强制类型转化得到新的变量`new-instance`
## Proj1: Data Structure
### Package
- package定义为一些java class的集合，它们共同实现一个特定的功能。
  - 如`org.junit.Assert.assertEquals`（称为canonical name）中`org.junit`是package名，`Assert`是类名，`assertEquals`是method名。用`import`语句引进后可以只用simple name`assertEquals`
  - 典型地，package name是写代码的机构的网络地址的倒序，如`org.junit`存储在junit.org中。
  - 当创建一个package时，需要通过在文件的第一行指定package名来说明该文件是该package的一部分：`package <package-name>`。
  - package的好处在于可以使得即使两个类的类名相同，但其canonical name必不相同，在使用时不会混淆。


## Lecture 12: Asymptotics
- 程序的运行效率：时间复杂度
  - 关心程序的每一行（每一个函数）被调用的次数随输入规模 $N$的关系，看 $N$的阶数($N\rightarrow +\infty$，且只需要看最高阶的无穷大是哪一阶)
  - $N,N\log N$是好的时间复杂度，再高阶在大规模时会很耗时。
  - 计算时间复杂度时只要考虑最坏情况（有时会提前返回，但不考虑这种情况）、忽略低阶无穷小、忽略系数、假设调用每个函数的时间相等
  - 即时间复杂度看的是整个算法中所有的调用个数是哪一阶的无穷大。
  - 简化的考虑方法是把所有需要恒定时间的步骤（不依赖于规模）看作单步操作。
  - $\Theta$定义：设 $R(N)$是一个函数，若 $R(N)\in [k_1f(N),k_2f(N)]$，记为 $R(N)\in \Theta(f(N))$（夹逼定理得到的同阶无穷小）。时间复杂度定义为作为 $N$的函数的runtime。若只知道上界，则记为 $O(f(N))$，若只知道下界，则记为 $\Omega(f(N))$
- 对于Lecture 7中的ArrayList，在进行添加操作时需要`resize`数组。此时应该考虑均摊的运行时间(amortized runtime)，若每次resize大小加1，则`addLast`的均摊运行时间为 $\Theta(N^2)$，而若每次resize大小乘2，则均摊运行时间为 $\Theta(N)$。
- MergeSort排序法：递归排序，把无序列表分为两半，分别排序，再merge；其中merge采用两边走指针法，时间复杂度为 $\Theta(n)$；于是总的时间复杂度为 $\Theta(N\log N)$

## Lecture 14: Disjoint Sets（不相交集合，并查集）
- Disjoint set数据类型有两个操作：`connect(x, y)`,`isConnected(x, y)`，分别连接两个元素以及判断两个元素是否相连（连接具有传递性）
- 需要关心如何实现这两个方法，来让set中元素数很多时操作也很快
  - 可以记录所有edge（很慢）
  - 由于连接具有传递性，本质上相连的任意两个元素之间都有edge，即相连的元素属于同一等价类。据此可以将set里的元素分成一系列disjoint的subsets
  - ListOfSetsDS：可以将并查集实现为一个set的list，但会很慢：如果没有任何连接，则需要把所有set遍历来判断两个元素是否在同一个set中，$\Theta(N)$
  - QuickFindDS：可以为实现为一个数组，每个位置代表一个元素，存储其所属的集合编号，`isConnect`为$\Theta(1)$（事实上是用数组的下标代表元素），但`connect`需要同时改变其中一个集合中所有元素的集合编号，需要查询这个集合包含哪些元素，$\Theta(N)$
  - QuickUnionDS：给每个元素分配一个父项（通过两个元素之间的连接关系），于是可以在数组的每个项中存储其父项的编号。此时`connect`实现为更改这个元素所属树的根节点的父项为另一个树的根节点（$\Theta(N)$），`isConnect`实现为看两个元素是否属于同一棵树（可以追溯到同一个父项）（$\Theta(N)$）
  - WeightedQuickUnionDS：QuickUnionDS的时间复杂度来源于当树很高时爬树的时间开销。于是我们需要控制树高，即在连接时选择把较小的树链接到较大的树上；这样就需要记录每棵树的大小
    - $2N$个元素的最差树是由一个 $N$元素最差树的根节点挂在另一个$N$元素最差树的根节点上，其时间复杂度为 $\Theta(\log N)$
    - 在根节点处以负数来记录，注意记录的是节点数而非最长的分支的长度，因为计算高度的逻辑比较难编写
  - WQU with Path Compression：QuickUnion要求树越短越好，即最好每个集合中所有元素都直接挂在根节点上，所以可以在每次`isConnect`爬树的时候把沿途遇到的节点改挂到根节点上，称为路径压缩
    - 可能会想，这样是不是和QuickFind没有区别了，QuickFind中在`connect`时需要更改集合中每个元素，而路径压缩在`isConnect`时需要更改路径上每个元素，但point在于路径压缩在worst case中的时间复杂度也不会是 $\Theta(N)$

## Lecture 16: Abstract Data Types(ADTs) & Binary Searching Trees(BST，二叉搜索树)
### ADT
- 抽象的数据类型，类似于interface，只关于可以做什么，而不管如何实现（如各种List和各种DisjointSet）
  - 标准库中有 `Collection` Interface，而 `List`, `Set`和 `Map`（键值对）都是从它之上extend出来的。

### BST
- 是为了构造`Set`和`Map`（即不重复的数据集合）
- 首先考虑一个OrderedLinkedList数据类型，naively提取和添加元素的时间复杂度都为 $\Theta(N)$
  - 优化提取元素的时间复杂度，可以利用已经排序的事实，进行二分搜索，时间复杂度变为 $\Theta(\log N)$。若想要二分搜索，就需要让sentinial指向中间的元素，中间的元素指向两个1/4的元素，以此类推，递归形成BST![alt text](note_img/BST.png)
- Tree的定义：由node和edge构成，任意两节点之间只有一条路径
  - Rooted tree: 选择一个节点称作root，于是root之外的每一个节点都有一个parent（定义为从该节点到root之间的path上的第一个节点）；若某个节点不是任何节点的parent，则称为leaf。
  - rooted binary tree: 每个节点至多有2个子节点。
- BST：BST定义为具有BST性质的rooted binary tree。其中BST性质定义为对于任意节点，其左subtree的任意节点都比该节点小，右subtree的任意节点都比该节点大（所以不能有两个节点具有重复的key）
- BST Interface
  - 需要有`contain()`方法，直接使用二分搜索即可
  - `insert()`方法：和`contain()`类似，先查找再插入（作为寻找的末尾节点的子节点），可以递归实现
    ```java
    static BST insert(BST T, Key ik) {
      if (T == null) {
        return new BST(ik);
      } else if (ik < T.key) {
        T.left = insert(T.left, ik);
      } else if (ik > T.key) {
        T.right = insert(T.right, ik);
      }
      return T
    }
    ```
  - Hibbard deletion: 删除节点可以分为删除具有0,1,2个子节点的节点三种情况
    - 0个子节点：直接删除
    - 1个子节点：把子节点挂到自己的位置
    - 2个子节点：可以把左子树的最右leaf或右子树的最左节点挂到自己的位置（这两种节点都最多有1个子节点），然后删除这个最右或最左节点（回到第2种情况）
- 用BST构造`set`：节点的key代表元素；构造`map`：在节点上既存储key又存储value

## Lecture 17: B-trees
- 若每个节点都有0个或2个子节点，则称为bushy tree。对于bushy tree，`contain()`的时间复杂度为 $\Theta(\log N)$；若每个节点都有0个或1个子节点，则是BST的worst case，时间复杂度为 $\Theta(N)$。
- 按照以上定义的`insert()`方法，从空树出发只有严格按照树的生长来Insert才能得到bushy tree，若按顺序insert只能得到worst case。有结论：随机插入也能得到bushy tree（但不总是能随机插入）
- 避免不平衡树的方法(B tree)：给定一个已经存在的bushy tree，不能增加新leaf，而是将新leaf存在旧leaf的节点中（所以tree的height不会再增加）；但若leaf变得太大，也会使搜索变慢，所以需要给每个leaf一个长度限制；若超出长度时，则把leaf的中间靠左的元素移动到其父节点中，然后将leaf被移动上去的元素左侧的元素分裂出来形成新的leaf。此时父节点有三个子节点：左（小于父节点元素）中（在父节点两个元素之间）右（大于父节点元素）。当root超出长度时，移动中间靠左的元素使其称为新的根节点。
  - 最大长度限制为3的B-tree也称为2-3-4 tree或2-4 tree（允许的子节点数目）
  - 最大长度限制为2的B-tree也称为2-3 tree

![alt text](note_img/B-tree.png)![alt text](note_img/B-tree-more.png)
- B tree的两个不变量：
  1. 所有的leaf都有相同的深度
  2. 若一个节点有k个元素，则其必须有k+1个子节点
- worst case performance：
  - 最好情况：每个节点都填满（L个）且有满子节点（L+1个），深度为 $\log_{L+1}N$，搜索时间复杂度为 $L\log_{L+1}N$
  - 最坏情况：满BST，只有一个节点有两个元素，深度为 $\log_2 (N-1)$，搜索时间复杂度为 $\log_2 (N-1)+1$
- B tree的特点
  1. 始终是平衡的
  2. 添加一个元素时永远是先和已有元素挤在同一个node中，如果overfull再分裂

## Lecture 18: Red Black Tree
### Tree Rotation
- 同样的 $N$个节点构成的BST可以有多种不同的结构，不同的结构之前可以通过旋转来相互转换。对于某个节点 `S`，可以定义左旋`LeftRotate(S)`和右旋`RightRotate(S)`。对于左旋，需要先将节点 `S`和其右侧的节点合并（此时成为一个2-3 tree），再将 `S`节点“发射到左下方”，并正确安排2-3tree的三个子节点；对于右旋，需要先将节点`S`和其左侧的节点合并称为一个2-3tree，再将 `S`节点“发射到右下方”。
- 平衡一个`N`元素的BST的时间复杂度（worst）是 $O(N)$。
![alt text](./note_img/tree-rotation.png)
### Left Leaning Red Black Tree(LLRB)
- 上述旋转方法是先任意添加元素，再通过旋转使其平衡。更好的做法是在每一步添加时都使其平衡。
#### LLRB
- LLRB定义为和2-3 tree一一对应的一种BST，它的构造方式是：把2-3tree的有2个元素的node中的两个元素分裂开（永远采取将左侧的元素向左下发射的分裂方式，于是left-leaning），用red edge连接（一般的edge是黑色的）![alt text](./note_img/LLRB.png)
- 2-3 tree构造比较复杂，通过这样的一一对应，我们无需构造复杂的B tree，又能得到完全平衡的BST。事实上LLRB的高度最多为其对应的2-3 tree的2倍左右，于是保持了`contain()`方法的$O(\log N)$复杂度

#### 通过旋转来保持BST是LLRB
- 若先构造2-3 tree再将其变为LLRB，则仍需构造复杂的2-3 tree。我们想要只构造一般的BST，只是在每次`add()`时都通过旋转使得BST保持为LLRB。旋转规则如下：
  1. 插入新元素时：由于2-3 tree插入新元素时总是先放入已有的node中而非增加新leaf，所以LLRB插入新元素时总是使用red edge。
  2. 若add元素创建的red edge是right leaning的，则需要通过把该edge的上节点left rotate来恢复；
  3. 连续两次向左添加red edge会导致没有对应的2-3 tree，此时需要把父节点向右旋转。 
  4. 若某个节点下有两个red edge（一个left leaning一个right leaning），对应的是2-3 tree的节点overfull的情况，此时需要分裂节点，即把这个父节点连接的3个edge颜色反转即可。![alt text](./note_img/LLRB-rotation.png)
- 需要注意：一次修复（旋转或反色操作）可能有副作用使得其它地方不满足LLRB要求。

## Lecture 19: Hashing
### motivation
- 至今最好的实现Set或Map的方式是LLRB，插入和查找的时间复杂度为 $\Theta(\log N)$，我们希望进一步优化。
- LLRB需要其中的元素是可以比较大小的，否则无法实现
### Hash table
- BST的思想是对元素进行排序，而Hash的思想是对元素进行分类。
- 考虑只包含整数的集合，可以按照整数的最后一位对其进行分类来加快查找，但有两个问题
  - 对于一般的非整数元素如何分类
  - 如果某一类中元素过多（分类并不随机）怎么办
  - 若给每一类预先分配相同的空间，若有些类没有填满则会导致空间浪费
  - 若分的bin数是固定的，那么和不分类的时间复杂度只差一个常数因子，没有用
- 以上问题的解决方法为
  - Reduction function：对整数使用取模的方法来分类，模数即为分的类数；一般而言若模数是质数，则可以得到随机的分类。
  - 可以不给每一类预先分配相同空间，而是用链表实现每一个bin，从而动态分配空间
  - 让分的bin个数 $M$ 随着总元素个数 $N$ 的增长而增长，需要保证 $M$ 始终至多只和 $N$相差一个常数因子：在bin的平均大小超过 $k$时增加 $M$（进行翻倍，类比ArrayList）。这样我们的所有操作在平均意义上都是 $\Theta(1)$的时间复杂度
  - 通过编码的方式来存储非整数对象
    - 因为我们不应该为每一种对象单独指定一种分类和resize的方式，所以集合只关心整数，而通过编码的方法把任意元素变成整数
    - base26编码把认为英文字符串是一个26进制string，将其转换为10进制即得到整数。
    - ASCII, unicode...
- 把所有元素都编码为整数，再取模进行分类的过程称为Hash function。问题在于：计算机能够存储的整数是有上下限的，所以需要将整数首先通过取模reduce到可以表示的整数区间。这样会导致重复表示，但概率较低，可以忽略。reduce后的整数称为hash code。

### Equality
- 每写一个新类时要override hashcode function，因为若这个新类有特别的相等关系（equals function），而默认的hashcode只是返回内存值，无法和equals function自洽。所以若两个对象是相等的，hashcode function的实现需要让它们具有相同的hashcode

### Mutability
- 若hash table需要使用对象作为key，则这个对象不能是mutable的。