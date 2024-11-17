

dates=(
  "2024-11-11"
  "2024-11-12"
  "2024-11-13"
  "2024-11-14"
  "2024-11-15"
  "2024-11-16"
  "2024-11-17"
)

for date in ${dates[@]}; do
  echo "Running script for $date"
  python dev/datagen.py --date $date
  echo "=========================="
done;

echo "showing all input data"
aws s3 ls pateash-dev/logs/


echo "showing all output data"
aws s3 ls pateash-dev/warehouse/logs_db/logs_iceberg/data/
