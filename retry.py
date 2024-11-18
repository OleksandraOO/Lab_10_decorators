import time

def retry(max_retries=3, delay=1):
    def decorator(func):
        def wrapper(*args, **kwargs):
            retries = 0
            while retries < max_retries:
                try:
                    return func(*args, **kwargs)
                except Exception as e:
                    retries += 1
                    print(f"Attempt {retries} failed: {e}. Retrying in {delay} seconds...")
                    time.sleep(delay)
            raise Exception(f"Function failed after {max_retries} attempts.")
        return wrapper
    return decorator

