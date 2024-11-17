import boto3
import random
import time
from datetime import datetime
import pytz
import pathlib
import argparse

# Create a session with the named profile
session = boto3.Session(profile_name="default")

# AWS S3 configuration
s3 = session.client("s3")

bucket_name = 'pateash-dev'  # replace with your actual bucket name


# Function to generate random IP addresses with some repetition for top IPs
def get_random_ip():
    ip_pool = [
        "184.87.250.135", "203.0.113.1", "192.0.2.60", "198.51.100.34",
        "203.0.113.2", "198.51.100.5", "198.51.100.10", "203.0.113.4",
        "192.0.2.25", "203.0.113.6", "203.0.113.3", "184.87.250.155"
    ]

    # Simulate a weighted choice to make certain IPs appear more frequently
    weighted_ips = random.choices(ip_pool, weights=[5, 5, 5, 5, 3, 3, 3, 2, 2, 2, 1, 1], k=1)
    return weighted_ips[0]


# Sample data for log generation
urls = ["/Integrated/challenge.gif", "/images/test.png", "/videos/sample.mp4"]

# Sample User-Agent strings with device types (Desktop, Mobile, Tablet)
user_agents = [
    # Desktop User-Agents
    (
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36",
    "Desktop"),
    (
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0 Safari/605.1.15",
    "Desktop"),

    # Mobile User-Agents
    (
    "Mozilla/5.0 (iPhone; CPU iPhone OS 14_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0 Mobile/15A5341f Safari/604.1",
    "Mobile"),
    (
    "Mozilla/5.0 (Linux; Android 10; SM-G975F) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Mobile Safari/537.36",
    "Mobile"),

    # Tablet User-Agents
    (
    "Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.2 Safari/605.1.15",
    "Tablet"),
    ("Mozilla/5.0 (Android 4.0.4; Tablet; rv:41.0) Gecko/41.0 Firefox/41.0", "Tablet")
]


# Function to select a weighted User-Agent
def get_random_user_agent():
    # Give a higher weight to certain User-Agents to simulate top devices
    weighted_user_agents = random.choices(user_agents, weights=[5, 5, 3, 3, 2, 2], k=1)
    return weighted_user_agents[0]  # returns (user_agent, device_type) tuple


def generate_timestamp_from_date(date_str, timezone=pytz.utc, format_string="%d/%b/%Y:%H:%M:%S +0000"):
    """
    Generate a timestamp string based on the given date (yyyy-mm-dd),
    timezone, and format.
    """
    # Parse the input date string to a datetime object
    provided_date = datetime.strptime(date_str, "%Y-%m-%d")

    # Localize the datetime to the provided timezone
    localized_date = timezone.localize(provided_date)

    # Format the datetime to the desired timestamp format
    return localized_date.strftime(format_string)

# Generate a single log line
def generate_log_line(date):
    ip = get_random_ip()
    timestamp = generate_timestamp_from_date(date)
    request_type = "GET"
    url = random.choice(urls)
    protocol = "HTTP/1.1"
    status_code = random.choice([200, 404, 500])
    response_size = random.randint(200, 5000)
    referrer = "-"

    user_agent, device_type = get_random_user_agent()  # Using the modified User-Agent selection
    log_line = f'{ip} - - [{timestamp}] "{request_type} {url} {protocol}" {status_code} {response_size} "{referrer}" "{user_agent} {device_type}"\n'
    return log_line


# Write log data to S3
def upload_logs_to_s3(date, num_lines=1000):
    log_data = ''.join(generate_log_line(date) for _ in range(num_lines))
    log_file_path = f'logs/simulated_logs_{date}.log'  # Path in the S3 bucket
    local_path=pathlib.Path(__file__).parent / log_file_path
    with open(local_path, "w") as file:
        file.write(log_data)
    # Upload to S3
    s3.put_object(
        Bucket=bucket_name,
        Key=log_file_path,
        Body=log_data
    )
    print(f"Uploaded {num_lines} log lines to s3://{bucket_name}/{log_file_path}")

def main():
    parser = argparse.ArgumentParser(description="Example script with arguments.")
    # Adding arguments
    parser.add_argument("--date", type=str, required=True, help="The date in yyyy-mm-dd format.")
    # parser.add_argument("--timezone", type=str, default="UTC", help="Timezone (default: UTC).")

    # Parse arguments
    args = parser.parse_args()

    # Access arguments
    date = args.date

    print(f"Provided date: {date}")
    # Run the log generation and upload
    upload_logs_to_s3(date,num_lines=1000)  # Generates and uploads 1000 log entries

if __name__ == "__main__":
    main()
