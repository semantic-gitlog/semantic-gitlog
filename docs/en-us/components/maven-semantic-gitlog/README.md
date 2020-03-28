# maven-semantic-gitlog

Changelog generator as maven plugin. Goals of this plugins:
- `semantic-gitlog:changelog`: generate changelog files.
- `semantic-gitlog:derive`: run same logic without generate with above and print the `nextVersion`.

## changelog

### Usage {docsify-ignore}

Refer to the usage [click here](/en-us/with-maven).

### Configuration {docsify-ignore}

| option | description |
| ------ | ----------- |
| `disabled` | Enable or disable `semantic-gitlog` module. Default is `false`. |
| `updatePomVersion` | Enable or disable automatic update the `${project.version}` value in `pom.xml`. Default is `false`. |

> [!TIP]
> Also refer to: [semantic-gitlog](/en-us/semantic-gitlog)

## derive

### Usage {docsify-ignore}

Refer to the usage [click here](/en-us/with-maven).

### Configuration {docsify-ignore}

| option | description |
| ------ | ----------- |
| `derivedVersionMark` | The value will output as a prefix with the version number when `derive` execute. Default is `null`. |

> [!TIP]
> Also refer to: [changelog](/en-us/maven-semantic-gitlog?id=changelog)
