# Gitlet Design Document

**Name**:

## Classes and Data Structures

----
### Main

用于判断命令行传入的argument是否构成有效的command，并调用`Repository`或`Commit`类中的相应Method。

#### Fields

没有字段


### Repository
用于改变和存储当前仓库的状态，包括初始化、stage文件等。
#### Fields

1. `public static final File CWD`: 存储CWD
2. `public static final File GITLET_DIR`: `.gitlet`配置文件夹的绝对路径
3. `public static final File COMMITS_DIR`: 存储各个commit的hash code的文件夹路径
4. `public static final File BLOBS_DIR`: 存储各个blobs的hash code的文件夹路径
5. `public static final File STAGED_DIR`: 存储stage files的文件夹路径
6. `private static Map<String, String> BRANCH_HEADERS`: 存储Commit tree所有branch的名称以及末端commit的hash code
7. `private static String HEAD`: 存储当前`HEAD`pointer的hash code

### Commit
直接操作commit tree。是一个tree类，但不直接存储子节点的Reference，而是存储子节点的hash code。需要包含的metadata只有timestamp和log message。以及需要存储repository中各个文件的blobs的hash code。

#### Fields

1. `public String Message`: 存储当前commit的log message
2. `public long TimeStamp`: 存储Unix历元开始的毫秒数
3. `public String SHA1`: 存储当前commit的hash code
4. `public Map<String, String> FileBlobs`: 存储各个文件对应的blob版本的hashcode
5. `public String[] ParentCommits`: 存储其parent commit的hash code

### Utils

#### Fields


## Algorithms

----
维护几个map：
1. `head` commit中有一个`Map<String, String> fileBlobs`（是否可以改成`Map<String, File>`?）
2. `config`中有一个`Map<String, String> stagedForAdd`



最大的问题是文件副本如何表示，如何判断两个副本是不同的副本。
blobs和commits都由hashcode标识，若两个blobs内容相同，则有相同的hashcode；若两个commit的各个属性都相同，则有相同的hashcode，称为content addressable。如何实现content addressable？使用SHA1方法。
需要实现的命令包括：
1. `init`: 创建文件夹以及创建initial commit（属于`master`分支）
2. `add`: 一次只能`add`一个文件，复制文件放入`staged`文件夹（以什么方式复制？直接复制，还是读取、serialization后复制？）若文件和当前commit相同则从`staged`中移除。
   - 分为：当前内容和head commit中相同/不同；该文件当前在staged/不在
3. `commit`: 默认情况下一个commit中文件应该和parent commit完全相同，除非该文件当前在`staged`中（parent commit和staged中的文件称为tracked，其它改动都是untracked，不会被commit）。`commit`后清空`staged`。
   - `commit`后把`staged`中的文件转移到`blobs`中
   - blobs content addressable，且可以保存整个文件的拷贝。
4. `rm`: 旨在untrack一个file。若该file位于staged，则unstage即可；若该file位于parent commit，则在stage中表明将其删除，并真的删掉这个文件。
5. `log`: 输出commit tree。使用`java.util.Date`和`java.util.Formatter`
## Persistence

----
文件结构为
```
CWD
└──.gitlet
    ├──commits
    ├──blobs
    └──staged
```

