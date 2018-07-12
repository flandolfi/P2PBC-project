# %%
from scipy import stats
import numpy as np
import pandas as pd
import matplotlib as mpl
import matplotlib.pyplot as plt
import seaborn as sns
import os

# %% MPL SETTINGS
mpl.rcdefaults()
# sns.set(font_scale=0.8)
sns.set_style("white")
# sns.set_palette("deep")
# sns.set_palette(['w', 'gray'])
mpl.rcParams['text.usetex'] = True
mpl.rcParams['font.size'] = 9
mpl.rcParams['font.family'] = [u'serif']
mpl.rcParams['font.serif'] = [u'Computer Modern']
mpl.rcParams['lines.linewidth'] = 1
mpl.rcParams['axes.linewidth'] = 1
mpl.rcParams['figure.figsize'] = (4, 3)

# %% FIGURES DIRECTORY
outdir = "./report/figures/"

if not os.path.exists(outdir):
    os.makedirs(outdir)

# %% BOOTSRAP
bs_lattice = pd.read_csv("./data/bootstrap/lattice.csv", index_col="Step")
bs_grid = pd.read_csv("./data/bootstrap/grid.csv", index_col="Step")
bs_growing = pd.read_csv("./data/bootstrap/growing.csv", index_col="Step")
bs_scalefree = pd.read_csv("./data/bootstrap/scale-free.csv", index_col="Step")
bs_random = pd.read_csv("./data/bootstrap/random.csv", index_col="Step")

# %%
bs_growing["AvgPathLength"].loc[0] = 0.
bs_grid["Degree"] = bs_grid["AvgInDegree"] + bs_grid["AvgOutDegree"]
bs_lattice["Degree"] = bs_lattice["AvgInDegree"] + bs_lattice["AvgOutDegree"]
bs_growing["Degree"] = bs_growing["AvgInDegree"] + bs_growing["AvgOutDegree"]
bs_scalefree["Degree"] = bs_scalefree["AvgInDegree"] + bs_scalefree["AvgOutDegree"]
bs_random["Degree"] = bs_random["AvgInDegree"] + bs_random["AvgOutDegree"]

# %%
fig, axes = plt.subplots(3, 1, sharex=True, figsize=(4, 5.5))
bs_grid.plot(y="AvgPathLength", label="Grid", ax=axes[0])
bs_lattice.plot(y="AvgPathLength", label="Lattice", ax=axes[0])
bs_growing.plot(y="AvgPathLength", label="Growing", ax=axes[0])
bs_scalefree.plot(y="AvgPathLength", label="Scale-Free", ax=axes[0])
bs_random.plot(y="AvgPathLength", label="Random", ax=axes[0])
bs_grid.plot(y="ClustCoeff", label="Grid", ax=axes[1])
bs_lattice.plot(y="ClustCoeff", label="Lattice", ax=axes[1])
bs_growing.plot(y="ClustCoeff", label="Growing", ax=axes[1])
bs_scalefree.plot(y="ClustCoeff", label="Scale-Free", ax=axes[1])
bs_random.plot(y="ClustCoeff", label="Random", ax=axes[1])
bs_grid.plot(y="Degree", label="Grid", ax=axes[2])
bs_lattice.plot(y="Degree", label="Lattice", ax=axes[2])
bs_growing.plot(y="Degree", label="Growing", ax=axes[2])
bs_scalefree.plot(y="Degree", label="Scale-Free", ax=axes[2])
bs_random.plot(y="Degree", label="Random", ax=axes[2])
axes[0].set_ylabel("Average Path Length")
axes[1].set_ylabel("Clustering Coefficient")
axes[2].set_ylabel("Degree")
axes[2].set_xlabel("Cycle")
axes[0].set_ylim((0, 10))
axes[1].set_ylim((0.2, 0.85))
axes[2].set_ylim(0, 45)
axes[2].set_xlim((0, 30))
axes[0].legend(loc='center left', bbox_to_anchor=(1, 0.5))
axes[1].legend(loc='center left', bbox_to_anchor=(1, 0.5))
axes[2].legend(loc='center left', bbox_to_anchor=(1, 0.5))
axes[0].axvline(20, color="gray", linestyle='--', linewidth=1.)
axes[1].axvline(20, color="gray", linestyle='--', linewidth=1.)
axes[2].axvline(20, color="gray", linestyle='--', linewidth=1.)
# axes[2].axhline(19, c="gray", linestyle=":")
fig.tight_layout()
plt.savefig("report/figures/bootstrap.pdf", bbox_inches='tight')
plt.show()


# %% NETWORKS
sf_c20 = pd.read_csv("./data/scale-free-networks/cache-20.csv")
sf_c40 = pd.read_csv("./data/scale-free-networks/cache-40.csv")
sf_c80 = pd.read_csv("./data/scale-free-networks/cache-80.csv")

# %%
fig, axes = plt.subplots(2, 1, sharex=True, figsize=(4, 4.4))
sf_c20.plot(x='Step', y='AvgPathLength', label="$c = 20$", ax=axes[0])
sf_c40.plot(x='Step', y='AvgPathLength', label="$c = 40$", ax=axes[0])
sf_c80.plot(x='Step', y='AvgPathLength', label="$c = 80$", ax=axes[0])
sf_c20.plot(x='Step', y='ClustCoeff', label="$c = 20$", ax=axes[1])
sf_c40.plot(x='Step', y='ClustCoeff', label="$c = 40$", ax=axes[1])
sf_c80.plot(x='Step', y='ClustCoeff', label="$c = 80$", ax=axes[1])
axes[0].set_ylabel("Average Path Length")
axes[1].set_ylabel("Clustering Coefficient")
axes[1].set_xlabel("Nodes")
axes[0].legend(loc='center left', bbox_to_anchor=(1, 0.5))
axes[1].legend(loc='center left', bbox_to_anchor=(1, 0.5))
axes[1].set_xscale('log', basex=2)
fig.tight_layout()
plt.savefig("report/figures/scale-free-networks.pdf", bbox_inches='tight')
plt.show()

# %% NODE REMOVAL
rr_c20 = pd.read_csv("./data/random-removal/cache-20.csv")
rr_c40 = pd.read_csv("./data/random-removal/cache-40.csv")
rr_c80 = pd.read_csv("./data/random-removal/cache-80.csv")

# %%
fig, ax = plt.subplots()
ax.plot(range(90, 101), range(1000, -1, -100), c='gray', linestyle='--', label="All Nodes")
rr_c20.plot(x='Step', y='LargestConnComponent', label="$c = 20$", ax=ax)
rr_c40.plot(x='Step', y='LargestConnComponent', label="$c = 40$", ax=ax)
rr_c80.plot(x='Step', y='LargestConnComponent', label="$c = 80$", ax=ax)
ax.set_ylabel("Size of Largest Connected Cluster")
ax.set_xlabel("Removed Nodes (\%)")
ax.set_ylim(0, 1000)
ax.legend(loc='center left', bbox_to_anchor=(1, 0.5))
fig.tight_layout()
plt.savefig("report/figures/random-removal.pdf", bbox_inches='tight')
plt.show()

# %% AGGREGATION -- AVG
varred_c20 = np.array(pd.read_csv("./data/aggregation/avg-C20-stats.csv").groupby(["Step"]).mean()['Std'])
varred_c22 = np.array(pd.read_csv("./data/aggregation/avg-C22-stats.csv").groupby(["Step"]).mean()['Std'])
varred_c25 = np.array(pd.read_csv("./data/aggregation/avg-C25-stats.csv").groupby(["Step"]).mean()['Std'])
varred_c40 = np.array(pd.read_csv("./data/aggregation/avg-C40-stats.csv").groupby(["Step"]).mean()['Std'])
varred_c80 = np.array(pd.read_csv("./data/aggregation/avg-C80-stats.csv").groupby(["Step"]).mean()['Std'])
varred_c20 = (varred_c20[1:]/varred_c20[:-1])**2
varred_c22 = (varred_c22[1:]/varred_c22[:-1])**2
varred_c25 = (varred_c25[1:]/varred_c25[:-1])**2
varred_c40 = (varred_c40[1:]/varred_c40[:-1])**2
varred_c80 = (varred_c80[1:]/varred_c80[:-1])**2

# %%
fig, ax = plt.subplots()
ax.plot(varred_c20, label="$c = 20$")
ax.plot(varred_c22, label="$c = 22$")
ax.plot(varred_c25, label="$c = 25$")
ax.plot(varred_c40, label="$c = 40$")
ax.plot(varred_c80, label="$c = 80$")
ax.axhline(0.5/np.sqrt(np.e), c='gray', linestyle='--', label="Theoretical")
ax.legend(loc='center left', bbox_to_anchor=(1, 0.5))
ax.set_xlabel("Cycle")
ax.set_ylabel("Variance Reduction Rate $(\\sigma_{i+1}/\\sigma_i)$");
ax.set_xlim(0, 40)
fig.tight_layout()
plt.savefig("report/figures/aggregation-avg.pdf", bbox_inches='tight')
plt.show()

# %% AGGREGATION -- MAX
def theoreticalModel(nodes, steps):
    p = p_0 = 1. - 1./nodes
    result = [p]

    for i in range(steps):
        p = p*p*(p_0**(nodes*(1 - p)))
        result.append(p)

    return result

# %%
max_c20 = pd.read_csv("./data/aggregation/max-C20-stats.csv")
max_c40 = pd.read_csv("./data/aggregation/max-C40-stats.csv")
max_c80 = pd.read_csv("./data/aggregation/max-C80-stats.csv")

# %%
max_c20["HasCorrectValue"] = 1. - max_c20["HasCorrectValue"]
max_c40["HasCorrectValue"] = 1. - max_c40["HasCorrectValue"]
max_c80["HasCorrectValue"] = 1. - max_c80["HasCorrectValue"]
max_c20 = max_c20[["Step", "HasCorrectValue"]].groupby(["Step"]).mean()
max_c40 = max_c40[["Step", "HasCorrectValue"]].groupby(["Step"]).mean()
max_c80 = max_c80[["Step", "HasCorrectValue"]].groupby(["Step"]).mean()

# %%
fig, ax = plt.subplots()
ax.plot(theoreticalModel(1024, 12), c="gray", linestyle="--", label="Theoretical")
max_c20.plot(y="HasCorrectValue", label="$c = 20$", ax=ax)
max_c40.plot(y="HasCorrectValue", label="$c = 40$", ax=ax)
max_c80.plot(y="HasCorrectValue", label="$c = 80$", ax=ax)
ax.set_xlabel("Cycle")
ax.set_ylabel("Proportion of Not-Reached Nodes");
ax.set_yscale("log")
ax.set_xlim(4, 9)
ax.set_ylim(0.001, 1)
ax.legend(loc='center left', bbox_to_anchor=(1, 0.5))
fig.tight_layout()
plt.savefig("report/figures/aggregation-max.pdf", bbox_inches='tight')
plt.show()

# %% AGGREGATION -- DIFFUSION
networks = dict()

for i in range(8, 15):
    networks[2**i] = pd.read_csv("./data/aggregation/diffusion-N{}-stats.csv".format(2**i))
    networks[2**i] = networks[2**i].groupby(["Step"]).mean()
    networks[2**i]["HasCorrectValue"] *= 100.

# %%
fig, ax = plt.subplots()

for i in range(8, 15):
    networks[2**i].plot(y="HasCorrectValue", label="$N = 2^{" + str(i) + "}$", ax=ax)

ax.set_xlabel("Cycle")
ax.set_ylabel("Nodes that Know the Exact Value (\%)");
ax.legend(loc='center left', bbox_to_anchor=(1, 0.5))
ax.set_xlim(0, 30)
fig.tight_layout()
plt.savefig("report/figures/aggregation-diffusion.pdf", bbox_inches='tight')
plt.show()

# %% CONNECTED COMPONENTS
cc = dict()

for i in range(1, 5):
    cc[2**i] = pd.read_csv("./data/connected-components/cache-C{}.csv".format(2**i))
    cc[2**i] = cc[2**i].groupby(["Step"]).mean()

# %%
fig, ax = plt.subplots()

for i in range(1, 5):
    cc[2**i].plot(y="ConnComponents", label="$c = {}$".format(2**i), ax=ax)

ax.set_xlabel("Cycle")
ax.set_ylabel("Connected Components");
ax.legend(loc='center left', bbox_to_anchor=(1, 0.5))
ax.set_xlim(0, 14)
fig.tight_layout()
plt.savefig("report/figures/connected-components.pdf", bbox_inches='tight')
plt.show()

# %% DEGREE DISTRIBUTION
def unfold(hist):
    result = np.array([])

    for degree, density in enumerate(hist):
        result = np.concatenate((result, degree*np.ones(density)))

    return result

# %%
dd_random = []
dd_scalefree = []

with open("./data/degree-distributions/random.csv") as file:
    lines = file.readlines()

    for line in lines:
        dd_random.append(unfold(np.fromstring(line, dtype=int, sep=',')[1:]))

with open("./data/degree-distributions/scale-free.csv") as file:
    lines = file.readlines()

    for line in lines:
        dd_scalefree.append(unfold(np.fromstring(line, dtype=int, sep=',')[1:]))

# %%
fig, ax = plt.subplots()

for cache, degrees in zip([10, 20, 30, 40], dd_random):
    sns.kdeplot(degrees, label="$c = {}$".format(cache), ax=ax)


ax.plot([stats.binom.pmf(i, 79, 0.5) for i in range(80)], c='gray', linestyle='--', label="Theoretical")
ax.set_xlabel("Degree")
ax.set_ylabel("PDF")
ax.legend().set_visible(False)
ax.legend(loc='center left', bbox_to_anchor=(1, 0.5))
fig.tight_layout()
plt.savefig("report/figures/degrees-random.pdf", bbox_inches='tight')
plt.show()

# %%
fig, ax = plt.subplots()

for cache, degrees in zip([10, 20, 30, 40], dd_scalefree):
    sns.kdeplot(degrees, label="$c = {}$".format(cache), ax=ax, bw=0.3)

ax.plot([np.inf] + [np.power(np.float(i), -3) for i in range(1, 80)], c='gray', linestyle='--', label="Theoretical")
ax.set_xlabel("Degree")
ax.set_ylabel("PDF")
ax.set_xlim(1, 80)
ax.set_ylim(0.00005, 1)
ax.set_xscale("log")
ax.set_yscale("log")
ax.legend(loc='center left', bbox_to_anchor=(1, 0.5))
fig.tight_layout()
plt.savefig("report/figures/degrees-scale-free.pdf", bbox_inches='tight')
plt.show()

# %% HEATMAP
heat_pi = pd.read_csv("./data/heatmap/pi-estimation.csv", index_col=0, header=None)
heat_size = pd.read_csv("./data/heatmap/size-estimation.csv", index_col=0, header=None)
heat_epidemic = pd.read_csv("./data/heatmap/epidemic.csv", index_col=0, header=None)

# %%
cmap = mpl.colors.ListedColormap(sns.color_palette('Blues', 256, 0.7))
fig, axes = plt.subplots(3, 5,
                         figsize=(5, 3.5),
                         gridspec_kw={
                            "width_ratios": [2, 2, 2, 2, 0.1],
                            "height_ratios": [1, 1, 1]
                        })

# %%
rows = 100
cols = 100

for i, step in enumerate(range(0, 31, 10)):
    img = axes[0, i].imshow(heat_size.as_matrix()[step, :].reshape((rows, cols)),
                            vmin=0,
                            vmax=2e-4,
                            cmap=cmap,
                            aspect='auto',
                            interpolation='none')
    axes[0, i].get_xaxis().set_ticks([])
    axes[0, i].get_yaxis().set_ticks([])


fig.colorbar(img, cax=axes[0, 4], ticks=[0, 1e-4, 2e-4], extend='max')
axes[0, 4].set_yticklabels([
    "$0$",
    """$1 \cdot 10^{-4}$""",
    """$2 \cdot 10^{-4}$"""
])
axes[0, 0].set_ylabel("Estimate $1/N$")

# %%
radius = 100

for i, step in enumerate(range(0, 31, 10)):
    img = axes[1, i].imshow(heat_pi.as_matrix()[step, :].reshape((radius, radius)),
                            vmin=0,
                            vmax=1,
                            cmap=cmap,
                            aspect='auto',
                            interpolation='none')
    axes[1, i].get_xaxis().set_ticks([])
    axes[1, i].get_yaxis().set_ticks([])

plt.colorbar(img, cax=axes[1, 4], ticks=[0, 0.25*np.pi, 1])
axes[1, 4].set_yticklabels(["$0$", """$\pi/4$""", "$1$"])
axes[1, 0].get_yaxis().set_visible(True)
axes[1, 0].set_ylabel("Estimate $\\pi/4$")

# %%
rows = 100
cols = 100

for i, step in enumerate(range(0, 31, 10)):
    img = axes[2, i].imshow(heat_epidemic.as_matrix()[step, :].reshape((rows, cols)),
                            vmin=0,
                            vmax=1,
                            cmap=cmap,
                            aspect='auto',
                            interpolation='none')
    axes[2, i].get_xaxis().set_ticks([])
    axes[2, i].get_yaxis().set_ticks([])
    axes[2, i].set_xlim(0, 99)
    axes[2, i].set_xlabel("Cycle {}".format(step))

plt.colorbar(img, cax=axes[2, 4], ticks=[0, 1])
axes[2, 0].set_ylabel("Estimate $max$")

# %%
fig.tight_layout()
fig.subplots_adjust(hspace=0.14)
plt.savefig("report/figures/heatmaps.pdf", bbox_inches='tight')
plt.show()

# %%
heat_pi_stats = pd.read_csv("./data/heatmap/pi-estimation-stats.csv", index_col="Step")
heat_size_stats = pd.read_csv("./data/heatmap/size-estimation-stats.csv", index_col="Step")
heat_epidemic_stats = pd.read_csv("./data/heatmap/epidemic-stats.csv", index_col="Step")
heat_pi_stats *= 100.
heat_size_stats *= 100.
heat_epidemic_stats *= 100.

# %%
fig, ax = plt.subplots()
heat_epidemic_stats.plot(y="HasCorrectValue", label="Max $(1)$", ax=ax)
heat_pi_stats.plot(y="HasCorrectValue", label="Avg $(\\pi/4)$", ax=ax)
heat_size_stats.plot(y="HasCorrectValue", label="Avg $(1/N)$", ax=ax)
ax.set_xlabel("Cycle")
ax.set_ylabel("Nodes that Know the Exact Value (\%)");
ax.legend(loc='center left', bbox_to_anchor=(1, 0.5))
ax.set_xlim(0, 60)
fig.tight_layout()
plt.savefig("report/figures/heatmap-stats.pdf", bbox_inches='tight')
plt.show()
