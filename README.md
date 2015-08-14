# subfixer
A CLI tool for subtitle delay fixing.

This tool does not fully sync the subs, it only applies a global (positive or negative) delay to all of the entries.

## Usage
```
java -jar subtitle-fixer.jar <delay pattern> <input file>
```
Where the &lt;delay pattern&gt; follows a [custom flavor](https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html#parse-java.lang.CharSequence-) of the ISO-8601 standard and the &lt;input file&gt; is the path for the subtitle file.

Optionally, there are two properties that can be specified via the `-Dproperty=value` option.

| Property | Possible values                             | Default |
|----------|---------------------------------------------|---------|
| logLevel | `OFF` `SEVERE` `INFO`                       | `OFF`   |
| encoding | `ISO-8859-1` `US-ASCII` `UTF-8` `UTF-16` .. | `UTF-8` |

## File format
The supported format for the subtitle file is the following:
```
<entry 1>
..
<entry N>
```
where the &lt;entry&gt; format:
```
<index><line break>
<start time> --> <end time><line break>
<text 1><line break>
..
<text Z><line break>
<line break>
```
- &lt;index&gt; range is [1..N]
- the timeframe (start and end times) has the format: *HH:mm:ss,SSS*
- the entry text can contain multiple lines, but at least 1 is required
- the &lt;line break&gt; is OS dependent
- the last &lt;line break&gt; is optional for the last entry of the file.

## Notes
I did this project entirely on default installation of Atom editor, without any fancy (in fact, none at all) Java IDE related features. Also, no internet access, yes, not even mobile networks - vacations they say :)

It was really, really fun. Every Java developer should try this every once in a while with a small project such as this. After a few years working with IDEs, such as Intellij or Eclipse, you'll miss every single feature of them, i can promise you that! :)

You'll learn, at least, that:

1. you do need to declare imports, no one will do that for you
2. you do need to declare imports, no one will do that for you
3. you do need to declare imports, no one will do that for you
4. imports. imports.

Well, this is almost a blog post, lets stop it here. Bottom line is, the code might have some weird formatting as well as unused imports and other smelly lines of code. I did not review the code nor implement the proper tests, yet.
