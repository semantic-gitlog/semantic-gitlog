# gradle-semantic-gitlog

Changelog generator as gradle plugin. Goals of this plugins:
- `changelog`: generate changelog files.
- `derive`: run same logic without generate with above and print the `nextVersion`.

## changelog

### Usage {docsify-ignore}

Refer to the usage [click here](/en-us/with-gradle).

### Configuration {docsify-ignore}

| option | description |
| ------ | ----------- |
| `disabled` | Enable or disable `semantic-gitlog` module. Default is `false`. |

> [!TIP]
> Also refer to: [semantic-gitlog](/en-us/semantic-gitlog)

## derive

### Usage {docsify-ignore}

Refer to the usage [click here](/en-us/with-gradle).

### Configuration {docsify-ignore}

| option | description |
| ------ | ----------- |
| `derivedVersionMark` | The value will output as a prefix with the version number when `derive` execute. Default is `null`. |

> [!TIP]
> Also refer to: [changelog](/en-us/gradle-semantic-gitlog?id=changelog)
