def print_msg(msg):
# This is the outer enclosing function

    def printer():
# This is the nested function
        print(msg)

    return printer

# We execute the function
# Output: Hello
function = print_msg("Hello");
print(function);
function();

