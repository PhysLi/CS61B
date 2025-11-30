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

  public static void addList(int x) {
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
  