def square(func):
    def wrapper(*args, **kwargs):
        result = func(*args, **kwargs)
        return result ** 2
    return wrapper

@square
def get_number():
    return 3

print(get_number())
