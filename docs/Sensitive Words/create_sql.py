def read_sensitive_words(file_path):
    # 读取敏感词文件，每行一个敏感词
    with open(file_path, 'r', encoding='utf-8') as file:
        words = [line.strip() for line in file if line.strip()]
    return words

def generate_sql_file(words, output_file_path):
    # 生成 .sql 文件并编写 insert 语句
    with open(output_file_path, 'w', encoding='utf-8') as file:
        file.write("INSERT INTO sensitive_word (word) VALUES\n")
        # 构造每一条插入语句
        values = [f"('{word}')" for word in words]
        file.write(",\n".join(values) + ";\n")

if __name__ == "__main__":
    input_file_path = 'sensitive_words.txt'  # 敏感词文件路径
    output_file_path = 'sensitive_words_insert.sql'  # 生成的 SQL 文件路径
    
    # 读取敏感词并生成 .sql 文件
    words = read_sensitive_words(input_file_path)
    if words:
        generate_sql_file(words, output_file_path)
        print(f"成功生成包含 {len(words)} 条插入记录的 SQL 文件：{output_file_path}")
    else:
        print("没有敏感词可生成 SQL 文件")
