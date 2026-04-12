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

## Persistence

----

