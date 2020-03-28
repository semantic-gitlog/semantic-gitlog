# gradle-semantic-gitlog

生成 `angular-style` 风格更新日志的 gradle 插件。插件任务：
- `changelog`: 生成更新日志文件。
- `derive`: 运行与上述目标一致的逻辑并输出 `nextVersion`，同时会跳过生成更新日志文件。

## changelog

### 用法 {docsify-ignore}

具体用法请[参考这里](/zh-cn/with-gradle).

### 配置 {docsify-ignore}

| 选项 | 说明 |
| ---- | ---- |
| `disabled` | 打开或关闭 `semantic-gitlog` 模块。默认为 `false`。 |

> [!TIP]
> 请同时参考：[semantic-gitlog](/zh-cn/semantic-gitlog)

## derive

### 用法 {docsify-ignore}

具体用法请[参考这里](/zh-cn/with-gradle).

### 配置 {docsify-ignore}

| 选项 | 说明 |
| ---- | ---- |
| `derivedVersionMark` | 执行 `derive` 时，该值作为前缀与版本号一起输出。默认为 `null`。 |

> [!TIP]
> 请同时参考：[changelog](/zh-cn/gradle-semantic-gitlog?id=changelog)
