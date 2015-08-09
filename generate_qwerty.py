file = open('clean.txt', 'r')
lines = file.readlines()

button_names = []
button_keycodes = []
for line in lines:
  tokens = line.split(' ')
  button_names.append(tokens[0])
  button_keycodes.append(int(tokens[1]))

buttons = []
unit = 1.0 / 18.0
adjust = 0.0
width = unit
def add_button(row, column):
    name = button_names.pop(0)
    keycode = button_keycodes.pop(0)
    button = "button %s %.5f %.5f %.5f %.5f %d" % (name, width, unit, column * unit + adjust, (row + 2.0) * unit, keycode)
    buttons.append(button)

def add_row(row, column, length):
    for i in range(length):
        add_button(row, column + i)

add_button(0, 0)
add_row(0, 2, 4)
adjust = unit / 2.0
add_row(0, 6, 4)
adjust = 0.0
add_row(0, 11, 4)
# second row
add_row(1, 0, 13)
width = unit * 2.0
add_button(1, 13)
# third row
width = unit * 1.5
add_button(2, 0)
width = unit
adjust = unit / 2.0
add_row(2, 1, 12)
width = unit * 1.5
add_button(2, 13)
# fourth row
width = unit * 1.85
adjust = 0.0
add_button(3, 0)
width = unit
adjust = unit * 1.85
add_row(3, 0, 11)
width = unit * 2
add_button(3, 11)
# fifth row
width = unit * 2.0
adjust = 0.0
add_button(4, 0)
width = unit
add_row(4, 2, 10)
# sixth row
add_row(5, 0, 3)
width = unit * 7.0
add_button(5, 3)
# purple keys
width = unit
add_row(0, 15, 3)
add_row(1, 15, 3)
add_row(2, 15, 3)
# arrow keys
add_button(4, 16)
add_row(5, 15, 3)

out = open("QWERTY_new.cfg", 'w')
out.write("image http_resources/qwerty_keyboard_text.png\n")
for button in buttons:
    out.write(button + '\n')

for button in buttons:
    print button
