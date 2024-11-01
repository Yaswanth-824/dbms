# generate_data_bookings.sql
from datetime import date, timedelta

with open('data_bookings.sql', 'w') as f:
    f.write("-- SQL Insert Statements for Bookings Table\n")
    start_date = date(2023, 1, 1)
    
    for i in range(1, 100001):
        user_id = i  # Reference to the user_id from Users table
        booking_date = start_date + timedelta(days=i % 365)  # Simulating booking dates
        booking_status = "Confirmed" if i % 2 == 0 else "Pending"  # Simulating different booking statuses
        f.write("INSERT INTO Bookings (user_id, booking_date, booking_status) VALUES ")
        if i < 100000:
            f.write(f"({user_id}, '{booking_date}', '{booking_status}');\n")
        else:
            f.write(f"({user_id}, '{booking_date}', '{booking_status}');\n")

            
# generate_sql.py
# generate_data_users.sql
# with open('data_users.sql', 'w') as f:
#     f.write("-- SQL Insert Statements for Users Table\n")
#     for i in range(1, 100001):
#         username = f"user{i}"  # Unique username
#         fname = f"FirstName{i}"
#         lname = f"LastName{i}"
#         email = f"user{i}@example.com"
#         phone_number = f"123456789{i % 10}"  # Simulating different phone numbers
#         f.write("INSERT INTO Users (username, email, phone_number) VALUES ")
#         if i < 100000:
#             f.write(f"('{username}', '{email}', '{phone_number}');\n")
#         else:
#             f.write(f"('{username}', '{email}', '{phone_number}');\n")
