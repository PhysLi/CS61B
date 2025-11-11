# CS61B
## Lecture 1: Introduction
- Java初步
    - 在Java中函数必须作为类的方法存在，用`public static`来声明一个函数
    - 所有变量（包括函数）都需有一个声明的类型，称为静态类型语言(static typed，使语言更快一些)。
    - `.java`文件先经过javac编译（需要检查所有类型是否与声明匹配）为`.class`文件，再通过java解释器运行。

## Lab01: Set up
### Git
- Local repository
    - Git是分布式版本管理系统，分布式意味着每个合作者的设备中都保存着项目的完整历史记录（项目的完整历史记录即称为仓库,repository）。
    - 将directory初始化为repository：命令行在该目录下运行`git init`
    - 指定directory中哪些文件是需要被版本管理的：`git add <filename>`，这个文件称为staged；每次对文件进行修改后，这些changes也需要被staged才能被进一步提交(committed)，所以修改之后需要重新进行 `git add`；也可以直接stage一整个文件夹。
    - 将所有被`add`的文件的当前版本提交到repository：`git commit -m "comment"`，会在repository中存储被track的文件的一个snapshot（`-m`的作用是可以添加一个comment）
    - 对最近的commit做补充：`git commit --amend`，可以是staged了一些忘记的更改，也可以是更改了comment。会覆盖最后一次commit。
    - 可以用`git status`查看当前repository状态，`git log`查看提交日志，提交日志里对每个提交都有一个ID，可以用`git show <id>`来具体查看某次提交的详细情况。
    - 版本回退：`git restore --staged <filename>`，可以将某个文件从staged变为modified（不再想提交某个更改）；若一个文件目前是unstaged状态，可以通过`git restore --source = <commit id or branch> <filename>`回退到某次commit的版本（但不影响commit历史），若省略`--source`则回退到最近一次commit的状态；
    - repository中文件的状态分为以下几种：

    ![](./note_img/file-status.png)
- Remote repository
    - `git clone <remote-repo-URL>`：在本地创建远程repo的一个copy，并以其最近的commit为范本创建working directory。同时记录该url来进行后续数据传输，称该远程repo为`origin`
    - `git pull <remote-repo-name> main`：获得远程repo中的最近文件的copy
    - `git push <remote-repo-name> main`：将本地文件的最近copy上传。
- Branching
    - 通过branching可以同时track项目的不同版本。默认的branch称为`main`。整个repo是树状结构，以commit作为节点
    - `git branch <new-branch-name>`：从当前branch创建新branch
    - `git switch <wanted-branch>`：将working directory切换到另一个branch；`git switch -c <new-branch-name>`能够创建同时切换。
    - `git branch -d <branch>`可以删除分支
    - `git branch -v`可以查看当前所处的是哪个分支
    - 在某个branch中执行`git merch <branch-to-merge>`可以将另一个分支和这个分支结合（通过创建一个新的commit）
