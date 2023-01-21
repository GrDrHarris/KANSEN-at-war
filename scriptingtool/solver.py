import numpy as np
from scipy import linalg

var = 1
while var == 1:
    n = int(input("n:"))
    A = []
    b = []
    for i in range(0, n):
        x = float(input("thickness:"))
        y = float(input("result:"))
        b.append(y)
        p = 1.0
        a = []
        for j in range(0, n):
            a.append(p)
            p = x * p
        A.append(a)
    A_ = np.array(A)
    b_ = np.array(b)
    x = linalg.solve(A_, b_)
    print(x)
